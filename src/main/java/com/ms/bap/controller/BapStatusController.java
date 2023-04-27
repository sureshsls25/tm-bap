package com.ms.bap.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ms.bap.services.action.BapStatusService;
import com.ms.bap.services.common.BGGateWaySearchService;
import com.ms.bap.util.ApplicationConstant;
import com.ms.bap.util.CommonUtil;
import com.ms.bap.util.JsonUtil;
import com.ms.common.dto.MessageResponse;
import com.ms.common.enums.ContextAction;
import com.ms.common.model.lookup.BGGateWayLookupRequest;
import com.ms.common.model.onsearch.OnSearchRequest;
import com.ms.common.model.onstatus.OnStatusRequest;
import com.ms.common.model.status.StatusRequest;
import org.json.JSONException;
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
import java.util.ArrayList;
import java.util.List;

@RestController
//@RequestMapping(ApplicationConstant.EXTERNAL_CONTEXT_ROOT)
public class BapStatusController {

	private static final Logger logger = LoggerFactory.getLogger(BapStatusController.class);
	@Autowired
	BapStatusService bapStatusService;
	@Autowired
	BGGateWaySearchService bgGateWaySearchService;
	@Autowired
	JsonUtil jsonUtil;

	@Autowired
	ObjectMapper objectMapper;
	@Value("${bap.no.status.data}")
	private String statusErrResponse;
	@Value("${bap.app.bg.domain}")
	private String bgDomain;

	@Value("${bap.app.bg.type}")
	private String bgType;

	@Value("${bap.app.bg.status}")
	private String bgStatus;

	private List<String> finalStatusResp= new ArrayList<>();


	@PostMapping(path = "/status", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> status(@RequestBody String body, @RequestHeader HttpHeaders httpHeaders) throws JsonProcessingException, UnknownHostException, JSONException, InterruptedException {

		StatusRequest request = (StatusRequest) jsonUtil.toObject(body, StatusRequest.class);
		String bppUrl= bgGateWaySearchService.getBGGatewayUri(new BGGateWayLookupRequest(bgDomain,bgType,bgStatus),httpHeaders);
		logger.info("Returned BPP Url {} ",bppUrl);
		String response = bapStatusService.getStatusResult(CommonUtil.buildBPPUrl(bppUrl, ContextAction.STATUS.value()),request, httpHeaders);
		logger.info("Order Status Response from BPP {} ",response);
		if(CommonUtil.isEmpty(response)) {
			return ResponseEntity
					.badRequest()
					.body(objectMapper.writeValueAsString(new MessageResponse(statusErrResponse)));
		}
		response= finalStatusResp.get(0);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping(path = "/on_status", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> on_status(@RequestBody String body, @RequestHeader HttpHeaders httpHeaders) throws JsonProcessingException, UnknownHostException {
		finalStatusResp.clear();
		OnStatusRequest request = (OnStatusRequest) jsonUtil.toObject(body, OnStatusRequest.class);
		String responseJson= objectMapper.writeValueAsString(request);
		finalStatusResp.add(responseJson);
		logger.info("Actual Status Response from BPP {}", finalStatusResp);
		return new ResponseEntity<>(finalStatusResp.get(0),HttpStatus.OK);
	}


}
