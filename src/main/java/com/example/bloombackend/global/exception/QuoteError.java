package com.example.bloombackend.global.exception;

import org.springframework.http.HttpStatus;

public enum QuoteError implements ErrorCode {

    QUOTE_DATA_NOT_EXIST(HttpStatus.NOT_FOUND, "QT1", "명언 데이터가 존재하지 않습니다."),
    QUOTE_NOT_FOUND(HttpStatus.NOT_FOUND, "QT2", "오늘 날짜의 명언이 존재하지 않습니다."),;

    private final HttpStatus httpStatusCode;
    private final String developCode;
    private final String message;

    QuoteError(final HttpStatus httpStatusCode, final String code, final String message) {
        this.httpStatusCode = httpStatusCode;
        this.developCode = code;
        this.message = message;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatusCode;
    }

    @Override
    public String getDevelopCode() {
        return developCode;
    }

    @Override
    public String getMessage() {
        return message;
    }
}