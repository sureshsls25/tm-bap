package com.ms.bap.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<Object> handleAccessDeniedException(
            Exception ex, WebRequest request) {
        return new ResponseEntity<Object>(
                "Access denied", new HttpHeaders(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ApiException.class)
    @ResponseBody
    public ResponseEntity<RestResponseBuilder> handleAuthenticationException(ApiException ex, HttpServletRequest request) {
        RestResponseBuilder message = RestResponse.builder()
                .status(ex.getStatus())
                .error("Api Exception")
                .message(ex.getMessage())
                .path(request.getRequestURI());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(message);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<RestResponseBuilder> genericException(Exception ex, HttpServletRequest request) {
        RestResponseBuilder message = RestResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .error("Generic Exception")
                .message(ex.getMessage())
                .path(request.getRequestURI());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
    }

}
