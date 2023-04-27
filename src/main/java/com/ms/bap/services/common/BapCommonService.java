package com.ms.bap.services.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ms.bap.builder.ConfirmBuilder;
import com.ms.bap.builder.SearchBuilder;
import com.ms.bap.builder.SelectBuilder;
import com.ms.bap.builder.StatusBuilder;
import com.ms.bap.util.CommonUtil;
import com.ms.bap.util.JsonUtil;
import com.ms.common.enums.ContextAction;
import com.ms.common.model.confirm.ConfirmRequest;
import com.ms.common.model.lookup.BGGateWayLookupRequest;
import com.ms.common.model.lookup.BGGateWayLookupResponse;
import com.ms.common.model.onconfirm.OnConfirmRequest;
import com.ms.common.model.onsearch.OnSearchRequest;
import com.ms.common.model.onselect.OnSelectRequest;
import com.ms.common.model.onstatus.OnStatusRequest;
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
    @Value("${bap.app.bpp_uri}")
    private String bppUri;

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
    public String sendSearchRequest(String bgSubscriberUrl, SearchRequest request, HttpHeaders httpHeaders) throws JsonProcessingException, UnknownHostException, InterruptedException {
        SearchRequest searchRequest= searchBuilder.buildSkillSearchRequest(bgSubscriberUrl,request);
        String json = objectMapper.writeValueAsString(searchRequest);
        logger.info("Constructed search request before send==== {}", json);
        String searchResp= sender.send(searchRequest.getContext().getBppUri(), httpHeaders, json);
        Thread.sleep(2000);
        OnSearchRequest onSearchResp= CommonUtil.getFinalSearchResp(searchRequest.getContext().getMessageId());
        logger.info("Generating  Final Resp from BPP search {}", onSearchResp);
        searchResp= objectMapper.writeValueAsString(onSearchResp.getMessage());
        return searchResp;
    }

    public String sendSelectedMentorRequest(SelectRequest request, HttpHeaders httpHeaders) throws JsonProcessingException, UnknownHostException {
        SelectRequest selectRequest= selectBuilder.buildSelectedMentorRequest(request);
        String selectUri = selectRequest.getContext().getBppUri().concat("/" + ContextAction.SELECT.value());
        String json = objectMapper.writeValueAsString(selectRequest);
        logger.info("Sending Request to BPP select {}", json);
        String selectResp= sender.sendWithTemplate(selectUri, httpHeaders, json);
        OnSelectRequest onSelectRequest= (OnSelectRequest) jsonUtil.toObject(selectResp,OnSelectRequest.class);
        logger.info("Getting  Resp from BPP select {}", onSelectRequest.getMessage());
        return selectResp;
    }

    public String sendConfirmRequest(ConfirmRequest request, HttpHeaders httpHeaders) throws JsonProcessingException, UnknownHostException {
        ConfirmRequest confirmRequest= confirmBuilder.buildConfirmRequest(request);
        String confirmUri = confirmRequest.getContext().getBppUri().concat("/" + ContextAction.CONFIRM.value());
        String json = objectMapper.writeValueAsString(confirmRequest);
        logger.info("confirmUri {}", confirmUri);
        logger.info("Sending Request to BPP confirm {}", json);
        String confirmResp= sender.sendWithTemplate(confirmUri, httpHeaders, json);
        OnConfirmRequest onConfirmRequest= (OnConfirmRequest) jsonUtil.toObject(confirmResp,OnConfirmRequest.class);
        logger.info("Getting  Resp from BPP confirm {}", onConfirmRequest.getMessage());
        return confirmResp;
    }

    public String sendStatusRequest(String bgSubscriberUrl, StatusRequest request, HttpHeaders httpHeaders) throws JsonProcessingException, UnknownHostException, InterruptedException {
        StatusRequest statusRequest= statusBuilder.buildStatusRequest(bgSubscriberUrl,request);
        //String statusUri = statusRequest.getContext().getBppUri();
        String statusUri = bppUri.concat("/" + ContextAction.STATUS.value());
        String json = objectMapper.writeValueAsString(statusRequest);
        logger.info("Sending Request to BPP status {}", json);
        String statusResp= sender.sendWithTemplate(statusUri, httpHeaders, json);
        OnStatusRequest onStatusRequest= (OnStatusRequest) jsonUtil.toObject(statusResp,OnStatusRequest.class);
        Thread.sleep(2000);
        logger.info("Getting  Resp from BPP status {}", onStatusRequest.getMessage());
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
