package com.ms.bap.builder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ms.common.enums.ContextAction;
import com.ms.common.model.common.Context;
import com.ms.common.model.common.Descriptor;
import com.ms.common.model.common.Intent;
import com.ms.common.model.common.Item;
import com.ms.common.model.search.SearchMessage;
import com.ms.common.model.search.SearchRequest;
import com.ms.common.model.status.StatusMessage;
import com.ms.common.model.status.StatusRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.UnknownHostException;

@Service
public class StatusBuilder {

    private static final Logger logger = LoggerFactory.getLogger(StatusBuilder.class);

    @Autowired
    private ResponseBuilder responseBuilder;

    @Autowired
    ObjectMapper objectMapper;


    public StatusRequest buildStatusRequest(String menteeEmailId) throws UnknownHostException{
        logger.info("buildStatusRequest called== {} {} ",menteeEmailId);
        StatusRequest request = new StatusRequest();
        Context context = this.responseBuilder.commonBuildContext(menteeEmailId, ContextAction.STATUS.value());
        logger.info("returned Context {} ",context);
        request.setContext(context);
        StatusMessage msg= buildStatusMessageBody(menteeEmailId);
        logger.info("returned msg {} ",msg);
        request.setMessage(msg);
        logger.info("Built status request {} ",request);
        return request;
    }

    public StatusMessage buildStatusMessageBody(String menteeEmailId){
        StatusMessage msg= new StatusMessage();
        msg.setMailId(menteeEmailId);
        logger.info("StatusMessage details== {}",msg);
        return msg;
    }




}
