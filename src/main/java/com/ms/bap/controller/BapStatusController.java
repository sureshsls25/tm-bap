package com.ms.bap.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ms.bap.services.action.BapStatusService;
import com.ms.bap.util.ApplicationConstant;
import com.ms.bap.util.CommonUtil;
import com.ms.bap.util.JsonUtil;
import com.ms.common.dto.MessageResponse;
import com.ms.common.model.onsearch.OnSearchRequest;
import com.ms.common.model.onstatus.OnStatusRequest;
import com.ms.common.model.status.StatusRequest;
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
@RequestMapping(ApplicationConstant.EXTERNAL_CONTEXT_ROOT)
public class BapStatusController {

	private static final Logger logger = LoggerFactory.getLogger(BapStatusController.class);
	@Autowired
	BapStatusService bapStatusService;
	@Autowired
	JsonUtil jsonUtil;

	@Autowired
	ObjectMapper objectMapper;
	@Value("${bap.no.status.data}")
	private String statusErrResponse;


	@GetMapping(path = "/status", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> status(@RequestParam("mailid") String mailId, @RequestHeader HttpHeaders httpHeaders) throws JsonProcessingException, UnknownHostException {

		//StatusRequest request = (StatusRequest) jsonUtil.toObject(body, StatusRequest.class);

		String response = bapStatusService.getStatusResult(mailId, httpHeaders);
		logger.info("Order Status Response from BG {} ",response);
		if(CommonUtil.isEmpty(response)) {
			return ResponseEntity
					.badRequest()
					.body(objectMapper.writeValueAsString(new MessageResponse(statusErrResponse)));
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping(path = "/on_status", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> on_search(@RequestBody String body, @RequestHeader HttpHeaders httpHeaders) throws JsonProcessingException, UnknownHostException {

		OnStatusRequest request = (OnStatusRequest) jsonUtil.toObject(body, OnStatusRequest.class);
		String responseJson= objectMapper.writeValueAsString(request);
		logger.info("Actual Status Response from BPP {}", responseJson);
		return new ResponseEntity<>(responseJson,HttpStatus.OK);
	}


}
