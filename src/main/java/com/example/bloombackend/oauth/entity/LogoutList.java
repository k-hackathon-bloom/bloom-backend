package com.example.bloombackend.oauth.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "logout_list")
public class LogoutList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token_hash", nullable = false, unique = true)
    private String tokenHash;

    @Column(name = "expiration_time", nullable = false)
    private LocalDateTime expirationTime;

    protected LogoutList() {}

    public LogoutList(String tokenHash, LocalDateTime expirationTime) {
        this.tokenHash = tokenHash;
        this.expirationTime = expirationTime;
    }
}