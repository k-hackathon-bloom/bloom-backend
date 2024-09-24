package com.example.bloombackend.bottlemsg.controller.dto.response.Info;

import lombok.Builder;

@Builder
public record BottleMessageInfo(
	Long messageId,
	String title,
	String content,
	String postCardUrl,
	String negativity
) {
}
