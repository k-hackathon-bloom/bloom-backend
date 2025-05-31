package com.example.bloombackend.bottlemsg.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "bottle_message_sent_log")
public class BottleMessageSentLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sent_log_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "message_id", nullable = false)
    @Getter
    private BottleMessageEntity message;

    @Column(name ="sender_id")
    private Long senderId;

    @Column(name = "is_saved", nullable = false)
    @Getter
    private Boolean isHide = false;

    @Builder
    public BottleMessageSentLog(BottleMessageEntity message, Long senderId) {
        this.senderId = senderId;
        this.message = message;
    }

    public void hide() {
        this.isHide = true;
    }
}
