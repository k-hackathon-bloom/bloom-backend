package com.example.bloombackend.fcm.service;

import com.example.bloombackend.fcm.controller.dto.FcmTokenRegisterRequest;
import com.example.bloombackend.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FcmService {
    UserRepository userRepository;

    public FcmService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public void updateFcmToken(Long userId, FcmTokenRegisterRequest request) {
        userRepository.updateFcmToken(userId, request.fcmToken());
    }
}