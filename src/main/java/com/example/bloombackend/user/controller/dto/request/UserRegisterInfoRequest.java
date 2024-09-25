package com.example.bloombackend.user.controller.dto.request;

public record UserRegisterInfoRequest(String nickname, String age, String gender, boolean isSurvey) {
}
