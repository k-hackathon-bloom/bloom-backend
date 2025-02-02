package com.example.bloombackend.user.repository;

public interface UserRepositoryCustom {
    void updateFcmToken(Long userId, String token);
}