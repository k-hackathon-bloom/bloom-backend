package com.example.bloombackend.fcm.controller;

import com.example.bloombackend.fcm.controller.dto.FcmTokenRegisterRequest;
import com.example.bloombackend.fcm.service.FcmService;
import com.example.bloombackend.global.config.annotation.CurrentUser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/fcm")
public class FcmController {

    private FcmService fcmService;

    public FcmController(FcmService fcmService) {
        this.fcmService = fcmService;
    }

    @PostMapping("/tokens")
    public ResponseEntity<Void> registerFcmToken(@RequestBody FcmTokenRegisterRequest request, @CurrentUser Long userId) {
        fcmService.updateFcmToken(userId, request);
        return ResponseEntity.ok().build();
    }
}