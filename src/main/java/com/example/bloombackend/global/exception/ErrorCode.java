package com.example.bloombackend.global.exception;

import org.springframework.http.HttpStatus;

public interface ErrorCode {
    String getDevelopCode();
    HttpStatus getHttpStatus();
    String getMessage();
}