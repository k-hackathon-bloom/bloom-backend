package com.example.bloombackend.global.exception;

import org.springframework.http.HttpStatus;

public enum QuestError implements ErrorCode {

    RECOMMEND_QUEST_AI_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Q1", "AI 응답에 문제가 발생했습니다. 기본 퀘스트를 반환합니다");

    private final HttpStatus httpStatusCode;
    private final String developCode;
    private final String message;

    QuestError(final HttpStatus httpStatusCode, final String code, final String message) {
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