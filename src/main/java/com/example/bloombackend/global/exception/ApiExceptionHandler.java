package com.example.bloombackend.global.exception;

import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(value = Integer.MIN_VALUE)
public class ApiExceptionHandler {

    @ExceptionHandler(value = RestApiException.class)
    public ResponseEntity<Object> apiException(RestApiException apiException) {
        ErrorCode errorCode = apiException.getErrorCode();
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(new ErrorResponse(errorCode.getDevelopCode(), errorCode.getMessage()));
    }
}