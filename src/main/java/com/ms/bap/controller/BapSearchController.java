package com.ms.bap.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ms.bap.services.action.BapSearchService;
import com.ms.bap.services.common.BGGateWaySearchService;
import com.ms.bap.util.ApplicationConstant;
import com.ms.bap.util.CommonUtil;
import com.ms.bap.util.JsonUtil;
import com.ms.common.dto.MessageResponse;
import com.ms.common.enums.ContextAction;
import com.ms.common.model.lookup.BGGateWayLookupRequest;
import com.ms.common.model.lookup.BGGateWayLookupResponse;
import com.ms.common.model.onsearch.OnSearchRequest;
import com.ms.common.model.search.SearchRequest;
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
import java.util.LinkedHashMap;

@RestController
//@RequestMapping(ApplicationConstant.EXTERNAL_CONTEXT_ROOT)
public class BapSearchController {

	private static final Logger logger = LoggerFactory.getLogger(BapSearchController.class);
	@Autowired
	private JsonUtil jsonUtil;
	@Autowired
	BapSearchService bapSearchService;

	@Autowired
	BGGateWaySearchService bgGateWaySearchService;
	@Autowired
	ObjectMapper objectMapper;
	@Value("${bap.no.search.data}")
	private String searchErrResponse;

	@Value("${bap.app.bg.domain}")
	private String bgDomain;

	@Value("${bap.app.bg.type}")
	private String bgType;

	@Value("${bap.app.bg.status}")
	private String bgStatus;

	@GetMapping(path = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> search(@RequestBody String body, @RequestHeader HttpHeaders httpHeaders) throws JsonProcessingException, UnknownHostException, JSONException, InterruptedException {

		SearchRequest request = (SearchRequest) jsonUtil.toObject(body, SearchRequest.class);
		String bppUrl= bgGateWaySearchService.getBGGatewayUri(new BGGateWayLookupRequest(bgDomain,bgType,bgStatus),httpHeaders);
		logger.info("Returned BPP Url {} ",bppUrl);
		String response = bapSearchService.getSkillSearchResult(CommonUtil.buildBPPUrl(bppUrl, ContextAction.SEARCH.value()), request, httpHeaders);
		logger.info("Returned Skill Search Response from BPP{} ",response);
		if(CommonUtil.isEmpty(response)) {
			return ResponseEntity
					.badRequest()
					.body(objectMapper.writeValueAsString(new MessageResponse(searchErrResponse)));
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping(path = "/on_search", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> on_search(@RequestBody String body, @RequestHeader HttpHeaders httpHeaders) throws JsonProcessingException, UnknownHostException {

		OnSearchRequest request = (OnSearchRequest) jsonUtil.toObject(body, OnSearchRequest.class);
		String responseJson= objectMapper.writeValueAsString(request);
		logger.info("Actual Response from BPP {}", responseJson);
		new CommonUtil().buildFinalSearchResp(request.getContext().getMessageId(), request);
		return new ResponseEntity<>(responseJson,HttpStatus.OK);
	}

}
