package com.ms.bap.builder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ms.bap.util.ApplicationConstant;
import com.ms.bap.util.CommonUtil;
import com.ms.common.enums.AckStatus;
import com.ms.common.model.common.Ack;
import com.ms.common.model.common.Context;
import com.ms.common.model.confirm.ConfirmRequest;
import com.ms.common.model.onsearch.OnSearchRequest;
import com.ms.common.model.response.Response;
import com.ms.common.model.response.ResponseMessage;
import com.ms.common.model.search.SearchRequest;
import com.ms.common.model.select.SelectRequest;
import com.ms.common.model.status.StatusRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.UUID;

@Service
public class ResponseBuilder {

    private static final Logger logger = LoggerFactory.getLogger(ResponseBuilder.class);
    @Value("${bap.app.domain}")
    private String domain;
    @Value("${bap.app.bap_id}")
    private String bapId;
    @Value("${bap.app.bap_uri}")
    private String bapUri;
    @Value("${bap.app.bpp_uri}")
    private String bppUri;
    @Value("${bap.app.bpp_id}")
    private String bppId;

    private String msgId;
    private String transId;

    @Autowired
    ObjectMapper objectMapper;

    public Context buildContext(String bgSubscriberUrl,String action) {
        logger.info("buildContext called == {} {}",bgSubscriberUrl, action);
        Context context= new Context();
        context.setDomain(this.domain);
        context.setAction(action);
        context.setBppUri(bgSubscriberUrl);
        //context.setBppUri(bppUri.concat("/search"));
        context.setBapId(bapId);
        context.setBapUri(bapUri);
        context.setTimestamp(CommonUtil.getDateTimeString(new Date()));
        context.setMessageId(UUID.randomUUID().toString());
        context.setTransactionId(UUID.randomUUID().toString());
        logger.info("Generated Skill Search Request Context== {}",context);
        return context;
    }

    public Context selectBuildContext(SelectRequest request, String action) {
        logger.info("selectBuildContext called == {}", action);
        OnSearchRequest searchResp= CommonUtil.getFinalSearchResp().values().stream().filter(req ->
                req.getMessage().getCatalog().getBppProviders().get(0).getAgent().getPerson().getId().equalsIgnoreCase(request.getMessage().getOrder().getItems().get(0).getId())).findAny().get();
        Context context = buildSearchContext(searchResp, action);
        return context;
    }

    public Context confirmBuildContext(ConfirmRequest request, String action) {
        logger.info("confirmBuildContext called == {}", action);
        OnSearchRequest searchResp= CommonUtil.getFinalSearchResp().values().stream().filter(req ->
                req.getMessage().getCatalog().getBppProviders().get(0).getAgent().getPerson().getId().equalsIgnoreCase(request.getMessage().getOrder().getProvider().getAgent().getPerson().getId())).findAny().get();
        Context context = buildSearchContext(searchResp, action);
        return context;
    }


    public Context buildSearchContext(OnSearchRequest searchResp, String action){
        Context context= new Context();
        if(!ObjectUtils.isEmpty(searchResp)){
            bppUri= searchResp.getContext().getBppUri();
            bapId= searchResp.getContext().getBapId();
            bapUri= searchResp.getContext().getBapUri();
            bppId=  searchResp.getContext().getBapId();
            msgId=  searchResp.getContext().getMessageId();
            transId=  searchResp.getContext().getTransactionId();
        }
        context.setDomain(this.domain);
        context.setAction(action);
        context.setBppUri(bppUri);
        context.setBapId(bapId);
        context.setBapUri(bapUri);
        context.setBppId(bppId);
        context.setTimestamp(CommonUtil.getDateTimeString(new Date()));
        context.setMessageId(msgId);
        context.setTransactionId(transId);
        logger.info("Generated "+action+" Request Context== {}",context);

        return context;
    }



    /*public ResponseEntity<String> sendAck(SearchRequest searchRequest) throws JsonProcessingException {
        Response response = new Response();
        ResponseMessage resMsg = new ResponseMessage();
        resMsg.setAck(new Ack(AckStatus.ACK));
        response.setMessage(resMsg);
        return new ResponseEntity<>(objectMapper.writeValueAsString(response), HttpStatus.OK);

    }*/


}
