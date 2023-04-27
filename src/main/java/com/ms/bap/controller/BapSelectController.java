package com.ms.bap.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ms.bap.services.action.BapSelectService;
import com.ms.bap.util.ApplicationConstant;
import com.ms.bap.util.CommonUtil;
import com.ms.bap.util.JsonUtil;
import com.ms.common.dto.MessageResponse;
import com.ms.common.model.onsearch.OnSearchRequest;
import com.ms.common.model.select.SelectRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.UnknownHostException;

@RestController
//@RequestMapping(ApplicationConstant.EXTERNAL_CONTEXT_ROOT)
public class BapSelectController {
	private static final Logger logger = LoggerFactory.getLogger(BapSelectController.class);
	@Autowired
	BapSelectService bapSelectService;
	@Autowired
	private JsonUtil jsonUtil;

	@Autowired
	ObjectMapper objectMapper;
	@Value("${bap.no.mentor.data}")
	private String selectErrResponse;

	@PostMapping("/select")
	private ResponseEntity<String> select(@RequestBody String body, @RequestHeader HttpHeaders httpHeaders) throws JsonProcessingException, UnknownHostException {

		SelectRequest request = (SelectRequest) jsonUtil.toObject(body, SelectRequest.class);

		String response = bapSelectService.getSelectedSearchDetails(request, httpHeaders);
		logger.info("Selected Mentor Response from BPP {} ",response);
		if(CommonUtil.isEmpty(response)) {
			return ResponseEntity
					.badRequest()
					.body(objectMapper.writeValueAsString(new MessageResponse(selectErrResponse)));
		}
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	@PostMapping(path = "/on_select", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> on_select(@RequestBody String body, @RequestHeader HttpHeaders httpHeaders) throws JsonProcessingException, UnknownHostException {

		OnSearchRequest request = (OnSearchRequest) jsonUtil.toObject(body, OnSearchRequest.class);
		String responseJson= objectMapper.writeValueAsString(request);
		logger.info("Actual select Response from BPP {}", responseJson);
		return new ResponseEntity<>(responseJson,HttpStatus.OK);
	}


}
