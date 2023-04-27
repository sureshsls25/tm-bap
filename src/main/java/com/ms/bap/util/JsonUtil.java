package com.ms.bap.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JsonUtil {

    @Autowired
    ObjectMapper objectMapper;

    public <Object> java.lang.Object toObject(String body, Class<?> object) throws JsonProcessingException {
        return objectMapper.readValue(body, object);
    }
}
