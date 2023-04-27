package com.ms.bap.services.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ms.bap.builder.ConfirmBuilder;
import com.ms.bap.builder.SearchBuilder;
import com.ms.bap.builder.SelectBuilder;
import com.ms.bap.builder.StatusBuilder;
import com.ms.bap.util.JsonUtil;
import com.ms.common.enums.ContextAction;
import com.ms.common.model.confirm.ConfirmRequest;
import com.ms.common.model.lookup.BGGateWayLookupRequest;
import com.ms.common.model.lookup.BGGateWayLookupResponse;
import com.ms.common.model.onsearch.OnSearchRequest;
import com.ms.common.model.search.SearchRequest;
import com.ms.common.model.select.SelectRequest;
import com.ms.common.model.status.StatusRequest;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.json.JSONArray;

import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.List;

@Service
public class BapCommonService {

    private static final Logger logger = LoggerFactory.getLogger(BapCommonService.class);

    @Value("${bap.app.bg.gateway.url}")
    private String bgGatewayUrl;
    @Autowired
    private SearchBuilder searchBuilder;
    @Autowired
    private ConfirmBuilder confirmBuilder;
    @Autowired
    private StatusBuilder statusBuilder;
    @Autowired
    private SelectBuilder selectBuilder;
    @Autowired
    SenderService sender;
    @Autowired
    JsonUtil jsonUtil;


    @Autowired
    ObjectMapper objectMapper;
    public String sendSearchRequest(String bgSubscriberUrl, String searchSkill, HttpHeaders httpHeaders) throws JsonProcessingException, UnknownHostException {
        SearchRequest searchRequest= searchBuilder.buildSkillSearchRequest(bgSubscriberUrl,searchSkill);
        String json = objectMapper.writeValueAsString(searchRequest);
        logger.info("Constructed search request before send==== {}", json);
        String searchResp= sender.send(searchRequest.getContext().getBppUri(), httpHeaders, json);
        logger.info("Getting  Resp from BPP search {}", searchResp);
        return searchResp;
    }

    public String sendSelectedMentorRequest(String mentorId, HttpHeaders httpHeaders) throws JsonProcessingException, UnknownHostException {
        SelectRequest selectRequest= selectBuilder.buildSelectedMentorRequest(mentorId);
        String selectUri = selectRequest.getContext().getBppUri().concat("/" + ContextAction.SELECT.value());
        String json = objectMapper.writeValueAsString(selectRequest);
        logger.info("Sending Request to BPP select {}", json);
        String selectResp= sender.send(selectUri, httpHeaders, json);
        logger.info("Getting  Resp from BPP select {}", selectResp);
        return selectResp;
    }

    public String sendConfirmRequest(String confirmId, HttpHeaders httpHeaders) throws JsonProcessingException, UnknownHostException {
        ConfirmRequest confirmRequest= confirmBuilder.buildConfirmRequest(confirmId);
        String confirmUri = confirmRequest.getContext().getBppUri().concat("/" + ContextAction.CONFIRM.value());
        String json = objectMapper.writeValueAsString(confirmRequest);
        logger.info("confirmUri {}", confirmUri);
        logger.info("Sending Request to BPP confirm {}", json);
        String confirmResp= sender.send(confirmUri, httpHeaders, json);
        logger.info("Getting  Resp from BPP confirm {}", confirmResp);
        return confirmResp;
    }

    public String sendStatusRequest(String menteeEmailId, HttpHeaders httpHeaders) throws JsonProcessingException, UnknownHostException {
        //String statusUri = statusRequest.getContext().getBppUri().concat("/" + ContextAction.STATUS.value());
        StatusRequest statusRequest= statusBuilder.buildStatusRequest(menteeEmailId);
        String statusUri = statusRequest.getContext().getBppUri().concat("/" + ContextAction.STATUS.value());
        String json = objectMapper.writeValueAsString(statusRequest);
        logger.info("Sending Request to BPP status {}", json);
        String statusResp= sender.send(statusUri, httpHeaders, json);
        logger.info("Getting  Resp from BPP status {}", statusResp);
        return statusResp;
    }

    public String send(BGGateWayLookupRequest gateWayLookupRequest, HttpHeaders httpHeaders) throws JsonProcessingException, UnknownHostException, JSONException {
        String json = objectMapper.writeValueAsString(gateWayLookupRequest);
        logger.info("Sending Request to BG lookup {}", json);
        String bgGatewayResp= sender.send(bgGatewayUrl.trim(), httpHeaders, json);
        logger.info("Getting  Resp from BG lookup {}", bgGatewayResp);
        List<BGGateWayLookupResponse> lookupResponses= objectMapper.readValue(bgGatewayResp, new TypeReference<List<BGGateWayLookupResponse>>() {

        });
        logger.info("lookupResponses {}", lookupResponses);
        return lookupResponses.get(0).getSubscriber_url();
    }

}
