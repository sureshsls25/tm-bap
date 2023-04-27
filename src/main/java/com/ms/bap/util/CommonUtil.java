package com.ms.bap.util;

import com.ms.bap.services.auth.UserDetailsImpl;
import com.ms.common.enums.ContextAction;
import com.ms.common.model.common.Descriptor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static com.ms.bap.util.ApplicationConstant.TIMESTAMP_FORMAT;

public class CommonUtil {

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
        return bppInputUrl.concat("/"+ContextAction.SEARCH.value());
    }
}
