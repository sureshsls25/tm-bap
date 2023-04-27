package com.ms.bap.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ms.bap.services.action.BapConfirmService;
import com.ms.bap.util.ApplicationConstant;
import com.ms.bap.util.CommonUtil;
import com.ms.bap.util.JsonUtil;
import com.ms.common.dto.MessageResponse;
import com.ms.common.model.confirm.ConfirmRequest;
import com.ms.common.model.onconfirm.OnConfirmRequest;
import com.ms.common.model.onsearch.OnSearchRequest;
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
public class BapConfirmController {

	private static final Logger logger = LoggerFactory.getLogger(BapConfirmController.class);
	@Autowired
	BapConfirmService bapConfirmService;
	@Autowired
	private JsonUtil jsonUtil;

	@Autowired
	ObjectMapper objectMapper;
	@Value("${bap.no.confirm.data}")
	private String confirmErrResponse;

	@PostMapping(path = "/confirm", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> confirm(@RequestBody String body, @RequestHeader HttpHeaders httpHeaders) throws JsonProcessingException, UnknownHostException {

		ConfirmRequest request = (ConfirmRequest) jsonUtil.toObject(body, ConfirmRequest.class);

		String response = bapConfirmService.getConfirmationResult(request, httpHeaders);
		logger.info("Confirmation Response from BPP {} ",response);
		if(CommonUtil.isEmpty(response)) {
			return ResponseEntity
					.badRequest()
					.body(objectMapper.writeValueAsString(new MessageResponse(confirmErrResponse)));
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping(path = "/on_confirm", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> on_confirm(@RequestBody String body, @RequestHeader HttpHeaders httpHeaders) throws JsonProcessingException, UnknownHostException {

		OnConfirmRequest request = (OnConfirmRequest) jsonUtil.toObject(body, OnConfirmRequest.class);
		String responseJson= objectMapper.writeValueAsString(request);
		logger.info("Actual confirm Response from BPP {}", responseJson);
		return new ResponseEntity<>(responseJson,HttpStatus.OK);
	}


}
