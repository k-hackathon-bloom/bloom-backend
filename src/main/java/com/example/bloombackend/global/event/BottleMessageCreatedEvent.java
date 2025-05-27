package com.example.bloombackend.global.event;

import com.example.bloombackend.bottlemsg.entity.BottleMessageEntity;

public record BottleMessageCreatedEvent(
    Long messageId,
    String content
) {
}
