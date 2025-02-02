package com.example.bloombackend.global.exception;

import org.springframework.http.HttpStatus;

public enum TokenError implements ErrorCode {

    INVALID_ACCESS_TOKEN(HttpStatus.BAD_REQUEST,"T1", "올바르지 않은 AccessToken입니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.BAD_REQUEST,"T2", "올바르지 않은 ReFreshToken입니다."),
    EXPIRED_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED,"T3", "만료된 AccessToken입니다."),
    EXPIRED_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "T4", "만료된 ReFreshToken입니다."),
    LOGGED_OUT_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED,"T5","로그아웃된 RefreshToken입니다."),
    NOT_ACCESS_TOKEN(HttpStatus.BAD_REQUEST,"T6","AccessToken이 아닙니다."),
    NOT_REFRESH_TOKEN(HttpStatus.BAD_REQUEST,"T7","RefreshToken이 아닙니다.");

    private final HttpStatus httpStatusCode;
    private final String developCode;
    private final String message;

    TokenError(final HttpStatus httpStatusCode, final String code, final String message) {
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