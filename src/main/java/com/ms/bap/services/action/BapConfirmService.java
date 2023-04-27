package com.ms.bap.services.action;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ms.bap.services.common.BapCommonService;
import com.ms.common.model.confirm.ConfirmRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.net.UnknownHostException;

@Service
public class BapConfirmService {

	private static final Logger logger = LoggerFactory.getLogger(BapConfirmService.class);

	@Autowired
	BapCommonService commonService;

	public String getConfirmationResult(String confirmId, HttpHeaders httpHeaders) throws JsonProcessingException, UnknownHostException {
		return commonService.sendConfirmRequest(confirmId,httpHeaders);
	}

}
