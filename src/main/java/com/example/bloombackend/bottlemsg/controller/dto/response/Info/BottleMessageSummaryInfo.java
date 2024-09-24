package com.example.bloombackend.bottlemsg.controller.dto.response.Info;

import lombok.Builder;

@Builder
public record BottleMessageSummaryInfo(
	Long messageId,
	String title,
	String postCardUrl,
	String negativity
) {
}
