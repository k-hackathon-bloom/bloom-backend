package com.example.bloombackend.bottlemsg.controller.dto.response;

import java.util.List;

import com.example.bloombackend.bottlemsg.controller.dto.response.Info.BottleMessageWithDateLogInfo;

public record ReceivedBottleMessagesResponse(List<BottleMessageWithDateLogInfo> bottleMessageResponses) {
}
