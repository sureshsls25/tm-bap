package com.ms.bap.builder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ms.bap.util.ApplicationConstant;
import com.ms.bap.util.CommonUtil;
import com.ms.common.enums.AckStatus;
import com.ms.common.model.common.Ack;
import com.ms.common.model.common.Context;
import com.ms.common.model.response.Response;
import com.ms.common.model.response.ResponseMessage;
import com.ms.common.model.search.SearchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
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

    @Autowired
    ObjectMapper objectMapper;

    public Context buildContext(String bgSubscriberUrl,String action) {
        logger.info("buildContext called == {} {}",bgSubscriberUrl, action);
        Context context= new Context();
        context.setDomain(this.domain);
        context.setAction(action);
        //context.setBppUri(bgSubscriberUrl);
        context.setBppUri(bppUri.concat("/search"));
        context.setBapId(bapId);
        context.setBapUri(bapUri);
        context.setTimestamp(CommonUtil.getDateTimeString(new Date()));
        context.setMessageId(UUID.randomUUID().toString());
        context.setTransactionId(UUID.randomUUID().toString());
        logger.info("Generated Skill Search Request Context== {}",context);
        return context;
    }

    public Context commonBuildContext(String searchItem,String action) {
        logger.info("commonBuildContext called == {} {}",searchItem, action);
        Context context= new Context();
        context.setDomain(this.domain);
        context.setAction(action);
        context.setBppUri(bppUri);
        context.setBapId(bapId);
        context.setBapUri(bapUri);
        context.setTimestamp(CommonUtil.getDateTimeString(new Date()));
        context.setMessageId(UUID.randomUUID().toString());
        context.setTransactionId(UUID.randomUUID().toString());
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
