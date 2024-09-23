package com.example.bloombackend.bottlemsg.controller.dto.response;

import java.util.List;

import com.example.bloombackend.bottlemsg.controller.dto.response.Info.BottleMessageSummaryInfo;

public record SentBottleMessageResponse(List<BottleMessageSummaryInfo> messages) {
}
