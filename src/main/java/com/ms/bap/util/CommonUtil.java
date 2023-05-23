package com.ms.bap.util;

import com.ms.bap.services.auth.UserDetailsImpl;
import com.ms.bap.services.common.BapCommonService;
import com.ms.common.enums.ContextAction;
import com.ms.common.model.common.Context;
import com.ms.common.model.onsearch.OnSearchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.TimeZone;

import static com.ms.bap.util.ApplicationConstant.TIMESTAMP_FORMAT;

public class CommonUtil {

    private static final Logger logger = LoggerFactory.getLogger(CommonUtil.class);
    private static LinkedHashMap<String, OnSearchRequest> finalSearchResp= new LinkedHashMap<>();

    public static LinkedHashMap<String, OnSearchRequest> getFinalSearchResp() {
        return finalSearchResp;
    }


    public static String getDateTimeString(Date date) {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat(TIMESTAMP_FORMAT);
        df.setTimeZone(tz);
        return df.format(date);
    }

    public static UserDetailsImpl getCurrentUserDetails() {
        return (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public static boolean isEmpty(Object request) {
        return ObjectUtils.isEmpty(request);
    }

    public static String buildBPPUrl(String bppInputUrl, String action){
        return bppInputUrl.concat("/"+action);
    }
    public static LinkedHashMap<String, OnSearchRequest> buildFinalSearchResp(String agentId, OnSearchRequest request){
        finalSearchResp.put(agentId, request);
        return finalSearchResp;
    }

    public static OnSearchRequest getFinalSearchResp(String messageId){
        return finalSearchResp.values().stream().filter(request-> messageId.equalsIgnoreCase(request.getContext().getMessageId())).findAny().get();
    }


}
