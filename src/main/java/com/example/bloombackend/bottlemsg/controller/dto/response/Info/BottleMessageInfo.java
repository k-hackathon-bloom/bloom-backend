package com.example.bloombackend.bottlemsg.controller.dto.response.Info;

import lombok.Builder;

import java.util.Optional;

@Builder
public record BottleMessageInfo(
		Long messageId,
		String title,
		String content,
		String postCardUrl,
		Optional<String> negativity
) {
}
