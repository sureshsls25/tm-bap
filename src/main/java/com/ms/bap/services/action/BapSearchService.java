package com.ms.bap.services.action;

import com.fasterxml.jackson.core.JsonProcessingException;

import com.ms.bap.services.common.BapCommonService;
import com.ms.common.model.search.SearchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.net.UnknownHostException;

@Service
public class BapSearchService {

	private static final Logger logger = LoggerFactory.getLogger(BapSearchService.class);
	@Autowired
	BapCommonService commonService;

	public String getSkillSearchResult(String bgSubscriberUrl, SearchRequest request, HttpHeaders httpHeaders) throws JsonProcessingException, UnknownHostException, InterruptedException {
		return commonService.sendSearchRequest(bgSubscriberUrl,request,httpHeaders);
	}

}
