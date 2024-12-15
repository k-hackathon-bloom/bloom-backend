package com.example.bloombackend.global.exception;

public class RestApiException extends RuntimeException {

    private final ErrorCode errorCode;

    public RestApiException(final ErrorCode errorCode) {
        super(errorCode.getDevelopCode());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}