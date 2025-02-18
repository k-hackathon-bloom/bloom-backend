package com.example.bloombackend.quote.controller;

import com.example.bloombackend.quote.controller.response.QuoteResponse;
import com.example.bloombackend.quote.service.QuoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/quote")
public class QuoteController {

    private final QuoteService quoteService;

    @Autowired
    public QuoteController(QuoteService quoteService) {
        this.quoteService = quoteService;
    }

    @GetMapping("")
    public ResponseEntity<QuoteResponse> getTodayQuote() {
        return ResponseEntity.ok(quoteService.getTodayQuote());
    }
}