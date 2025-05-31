package com.example.bloombackend.bottlemsg.entity;

import com.example.bloombackend.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.Builder;


public class BottleMessageSentLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "receipt_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "message_id", nullable = false)
    private BottleMessageEntity message;

    @Column(name = "is_saved", nullable = false)
    private boolean isSaved = true;

    @Builder
    public BottleMessageSentLog(BottleMessageEntity message, UserEntity recipient) {
        this.message = message;
    }

    public void delete() {
        this.isSaved = false;
    }
}
