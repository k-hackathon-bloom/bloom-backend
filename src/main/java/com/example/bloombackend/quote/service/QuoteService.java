package com.example.bloombackend.quote.service;

import com.example.bloombackend.global.exception.QuoteError;
import com.example.bloombackend.global.exception.RestApiException;
import com.example.bloombackend.quote.controller.response.QuoteResponse;
import com.example.bloombackend.quote.entity.QuoteEntity;
import com.example.bloombackend.quote.repository.QuoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class QuoteService {

    private final QuoteRepository quoteRepository;

    @Autowired
    public QuoteService(QuoteRepository quoteRepository) {
        this.quoteRepository = quoteRepository;
    }

    public QuoteResponse getTodayQuote() {
        QuoteEntity quote = quoteRepository.findById(getTodayQuoteId())
                .orElseThrow(() -> new RestApiException(QuoteError.QUOTE_NOT_FOUND));
        return quote.toResponse();
    }

    private Long getTodayQuoteId() {
        long totalCount = quoteRepository.count();
        if (totalCount == 0) throw new RestApiException(QuoteError.QUOTE_DATA_NOT_EXIST);
        long epochDay = LocalDate.now().toEpochDay();
        return epochDay % totalCount + 1;
    }
}