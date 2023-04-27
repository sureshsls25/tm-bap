package com.ms.bap.builder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ms.common.enums.ContextAction;
import com.ms.common.model.common.*;
import com.ms.common.model.confirm.ConfirmMessage;
import com.ms.common.model.confirm.ConfirmRequest;
import com.ms.common.model.search.SearchMessage;
import com.ms.common.model.search.SearchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.UnknownHostException;

@Service
public class ConfirmBuilder {

    private static final Logger logger = LoggerFactory.getLogger(ConfirmBuilder.class);

    @Autowired
    private ResponseBuilder responseBuilder;

    @Autowired
    ObjectMapper objectMapper;


    public ConfirmRequest buildConfirmRequest(String confirmId) throws UnknownHostException{
        logger.info("buildConfirmRequest called== {} ",confirmId);
        ConfirmRequest request = new ConfirmRequest();
        Context context = this.responseBuilder.commonBuildContext(confirmId, ContextAction.CONFIRM.value());
        logger.info("returned Context {} ",context);
        request.setContext(context);
        ConfirmMessage msg= buildSearchMessageBody(confirmId);
        request.setMessage(msg);
        logger.info("Built Confirm request {} ",request);
        return request;
    }

    public ConfirmMessage buildSearchMessageBody(String confirmId){
        ConfirmMessage msg= new ConfirmMessage();
        Order order= new Order();
        Provider provider= new Provider();
        Agent agent= new Agent();
        Person person= new Person();
        person.setId(confirmId);
        agent.setPerson(person);
        provider.setAgent(agent);
        order.setProvider(provider);
        msg.setOrder(order);
        logger.info("Confirm Message body details== {}",msg);
        return msg;
    }




}
