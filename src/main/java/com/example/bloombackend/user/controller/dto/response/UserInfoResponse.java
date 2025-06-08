package com.example.bloombackend.user.controller.dto.response;

import com.example.bloombackend.credit.service.dto.UserCreditInfo;
import com.example.bloombackend.user.controller.dto.response.info.UserInfo;

import lombok.Builder;

@Builder
public record UserInfoResponse(UserInfo userInfo, UserCreditInfo creditInfo) {
}
