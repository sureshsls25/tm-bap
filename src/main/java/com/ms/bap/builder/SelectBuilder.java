package com.ms.bap.builder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ms.common.enums.ContextAction;
import com.ms.common.model.common.*;
import com.ms.common.model.search.SearchMessage;
import com.ms.common.model.search.SearchRequest;
import com.ms.common.model.select.SelectMessage;
import com.ms.common.model.select.SelectRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

@Service
public class SelectBuilder {

    private static final Logger logger = LoggerFactory.getLogger(SelectBuilder.class);

    @Autowired
    private ResponseBuilder responseBuilder;

    @Autowired
    ObjectMapper objectMapper;


    public SelectRequest buildSelectedMentorRequest(String mentorId) throws UnknownHostException{
        logger.info("buildSelectedMentorRequest called== {} {} ",mentorId);
        SelectRequest request = new SelectRequest();
        Context context = this.responseBuilder.commonBuildContext(mentorId, ContextAction.SELECT.value());
        logger.info("returned Context {} ",context);
        request.setContext(context);
        SelectMessage msg= buildSelectedMentorMessageBody(mentorId);
        request.setMessage(msg);
        logger.info("Built selected Mentor request {} ",request);
        return request;
    }

    public SelectMessage buildSelectedMentorMessageBody(String mentorId){
        SelectMessage msg= new SelectMessage();
        Order order= new Order();
        List<Item> items= new ArrayList<>();
        Item item= new Item();
        item.setId(mentorId);
        items.add(item);
        order.setItems(items);
        msg.setOrder(order);
        logger.info("SelectedMentor Message Body details== {}",msg);
        return msg;
    }




}
