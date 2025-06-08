package com.example.bloombackend.bottlemsg.controller.dto.response.Info;

import lombok.Builder;

import java.util.Optional;

@Builder
public record BottleMessageSummaryInfo(
		Long messageId,
		String title,
		String postCardUrl,
		Optional<String> negativity
) {
}
