package com.example.bloombackend.global.event;

public record BottleMessageCreatedEvent(
    Long messageId,
    String content,
    Long userId
) {
}
