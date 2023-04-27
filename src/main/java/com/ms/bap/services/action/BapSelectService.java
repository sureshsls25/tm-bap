package com.ms.bap.services.action;

import com.fasterxml.jackson.core.JsonProcessingException;

import com.ms.bap.services.common.BapCommonService;
import com.ms.common.model.select.SelectRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.net.UnknownHostException;

@Service
public class BapSelectService {

	private static final Logger logger = LoggerFactory.getLogger(BapSelectService.class);

	@Autowired
	BapCommonService commonService;
	public String getSelectedSearchDetails(String mentorId, HttpHeaders httpHeaders) throws UnknownHostException, JsonProcessingException {
		return commonService.sendSelectedMentorRequest(mentorId,httpHeaders);
	}

}
