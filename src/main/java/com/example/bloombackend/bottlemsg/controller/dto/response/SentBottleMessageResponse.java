package com.example.bloombackend.bottlemsg.controller.dto.response;

import java.util.List;

import com.example.bloombackend.bottlemsg.controller.dto.response.Info.SentBottleMessageInfo;

public record SentBottleMessageResponse(List<SentBottleMessageInfo> messages) {
}
