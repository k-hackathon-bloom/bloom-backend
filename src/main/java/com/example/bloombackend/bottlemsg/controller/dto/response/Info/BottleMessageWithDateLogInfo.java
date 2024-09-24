package com.example.bloombackend.bottlemsg.controller.dto.response.Info;

public record BottleMessageWithDateLogInfo(
	BottleMessageLogInfo log,
	BottleMessageSummaryInfo messages) {
}

