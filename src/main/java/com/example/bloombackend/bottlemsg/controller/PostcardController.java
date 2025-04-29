package com.example.bloombackend.bottlemsg.controller;

import com.example.bloombackend.bottlemsg.controller.dto.response.PostcardsResponse;
import com.example.bloombackend.bottlemsg.controller.dto.response.ReceivedBottleMessagesResponse;
import com.example.bloombackend.bottlemsg.sevice.PostcardService;
import com.example.bloombackend.global.config.annotation.CurrentUser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/postcard")
public class PostcardController {
    private final PostcardService postcardService;

    public PostcardController(PostcardService postcardService) {
        this.postcardService = postcardService;
    }

    @GetMapping("/all")
    public ResponseEntity<PostcardsResponse> getAllPostcards(@CurrentUser Long userId) {
        return ResponseEntity.ok(postcardService.getAllPostcards());
    }
}
