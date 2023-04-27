package com.ms.bap.builder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ms.bap.dao.UsersRepo;
import com.ms.bap.dto.UsersDTO;
import com.ms.bap.entity.Users;
import com.ms.bap.exception.ApiException;
import com.ms.bap.services.auth.UserDetailsImpl;
import com.ms.bap.util.CommonUtil;
import com.ms.common.dto.SkillDto;
import com.ms.common.enums.ContextAction;
import com.ms.common.model.common.*;
import com.ms.common.model.confirm.ConfirmMessage;
import com.ms.common.model.confirm.ConfirmRequest;
import com.ms.common.model.search.SearchMessage;
import com.ms.common.model.search.SearchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ConfirmBuilder {

    private static final Logger logger = LoggerFactory.getLogger(ConfirmBuilder.class);

    @Autowired
    private ResponseBuilder responseBuilder;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    UsersRepo userRepository;


    public ConfirmRequest buildConfirmRequest(ConfirmRequest request) throws UnknownHostException{
        logger.info("buildConfirmRequest called== {} ");
        Context context = this.responseBuilder.confirmBuildContext(request, ContextAction.CONFIRM.value());
        logger.info("returned Context {} ",context);
        request.setContext(context);
        ConfirmMessage msg= buildSearchMessageBody(request);
        request.setMessage(msg);
        logger.info("Built Confirm request {} ",request);
        return request;
    }

    public ConfirmMessage buildSearchMessageBody(ConfirmRequest request){
        ConfirmMessage msg= new ConfirmMessage();
        Order order= new Order();
        Provider provider= new Provider();
        Agent agent= new Agent();
        Person person= new Person();
        List<SkillDto> skills= new ArrayList<>();
        SkillDto skill= new SkillDto();
        skill.setName(request.getMessage().getOrder().getProvider().getSkill().get(0).getName());
        skills.add(skill);
        List<Fulfillment> fulfillments= new ArrayList<>();
        UserDetailsImpl userDetails = CommonUtil.getCurrentUserDetails();
        Fulfillment fulfillment= new Fulfillment();
        Customer customer= new Customer();
        Person mentee= new Person();
        mentee.setMailid(userDetails.getEmail());
        mentee.setName(userDetails.getFullname());
        customer.setPerson(mentee);
        fulfillment.setCustomer(customer);
        fulfillments.add(fulfillment);
        person.setId(request.getMessage().getOrder().getProvider().getAgent().getPerson().getId());
        agent.setPerson(person);
        provider.setAgent(agent);
        provider.setSkill(skills);
        provider.setFulfillments(fulfillments);
        order.setProvider(provider);
        msg.setOrder(order);
        logger.info("Confirm Message body details== {}",msg);
        return msg;
    }






}
