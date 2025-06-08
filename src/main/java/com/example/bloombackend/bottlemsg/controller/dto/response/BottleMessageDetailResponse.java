package com.example.bloombackend.bottlemsg.controller.dto.response;

import com.example.bloombackend.bottlemsg.controller.dto.response.Info.BottleMessageInfo;

public record BottleMessageDetailResponse(
	BottleMessageInfo message,
	BottleMessageReactionResponse reaction
) {
}
