package com.ms.bap.exception;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RestResponse {
    private static final long serialVersionUID = 1L;
    private final LocalDateTime timestamp = LocalDateTime.now();
    private int status;
    private String error;
    private String message;
    private String path;

    public static RestResponseBuilder builder() {
        return new RestResponseBuilder();
    }

}
