package com.ms.bap.services.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ms.bap.services.action.BapSelectService;
import com.ms.common.model.lookup.BGGateWayLookupRequest;
import com.ms.common.model.select.SelectRequest;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.net.UnknownHostException;

@Service
public class BGGateWaySearchService
{
    private static final Logger logger = LoggerFactory.getLogger(BGGateWaySearchService.class);

    @Autowired
    BapCommonService commonService;
    public String getBGGatewayUri(BGGateWayLookupRequest gateWayLookupRequest, HttpHeaders httpHeaders) throws UnknownHostException, JsonProcessingException, JSONException {
        return commonService.send(gateWayLookupRequest,httpHeaders);
    }
}
