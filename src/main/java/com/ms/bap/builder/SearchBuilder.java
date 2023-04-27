package com.ms.bap.builder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ms.common.enums.ContextAction;
import com.ms.common.model.common.Context;
import com.ms.common.model.common.Descriptor;
import com.ms.common.model.common.Intent;
import com.ms.common.model.common.Item;
import com.ms.common.model.search.SearchMessage;
import com.ms.common.model.search.SearchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.UnknownHostException;

@Service
public class SearchBuilder {

    private static final Logger logger = LoggerFactory.getLogger(SearchBuilder.class);

    @Autowired
    private ResponseBuilder responseBuilder;

    @Autowired
    ObjectMapper objectMapper;


    public SearchRequest buildSkillSearchRequest(String bgSubscriberUrl, String searchSkill) throws UnknownHostException{
        logger.info("buildSkillSearchRequest called== {} {} ",bgSubscriberUrl, searchSkill);
        SearchRequest request = new SearchRequest();
        Context context = this.responseBuilder.buildContext(bgSubscriberUrl, ContextAction.SEARCH.value());
        logger.info("returned Context {} ",context);
        request.setContext(context);
        SearchMessage msg= buildSearchMessageBody(searchSkill);
        logger.info("returned msg {} ",msg);
        request.setMessage(msg);
        logger.info("Built search request {} ",request);
        return request;
    }

    public SearchMessage buildSearchMessageBody(String searchSkill){
        SearchMessage msg= new SearchMessage();
        Intent intent= new Intent();
        Item item= new Item();
        Descriptor descriptor= new Descriptor();
        descriptor.setName(searchSkill);
        item.setDescriptor(descriptor);
        intent.setItem(item);
        msg.setIntent(intent);
        logger.info("SearchMessage details== {}",msg);
        return msg;
    }




}
