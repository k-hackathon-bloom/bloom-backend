package com.example.bloombackend.bottlemsg.service;


import com.example.bloombackend.bottlemsg.controller.dto.request.CreateBottleMessageRequest;
import com.example.bloombackend.bottlemsg.service.dto.EmotionScore;
import com.example.bloombackend.bottlemsg.service.dto.SentimentAnalysisDto;
import com.example.bloombackend.global.AIUtil;
import com.example.bloombackend.global.event.BottleMessageCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class BottleMessageEventListener {
    private final BottleMessageService bottleMessageService;
    private final AIUtil aiUtil;

    public BottleMessageEventListener(BottleMessageService bottleMessageService, AIUtil aiUtil) {
        this.bottleMessageService = bottleMessageService;
        this.aiUtil = aiUtil;
    }

    @Async
    @EventListener
    public void handle(BottleMessageCreatedEvent event) {
        SentimentAnalysisDto analyze = analysisMessage(AnalyzeMessagePrompt.createAIPrompt(event.content()));
        bottleMessageService.updateMessageAnalysisResult(event.messageId(), analyze);
    }

    private SentimentAnalysisDto analysisMessage(String content) {
        String response = aiUtil.generateCompletion(content);
        return parseSentimentString(response);
    }

    public SentimentAnalysisDto parseSentimentString(String input) {
        log.info("Parsing Sentiment string: {}", input);
        List<EmotionScore> emotions = new ArrayList<>();

        String[] lines = input.split("\n");

        for (int i = 2; i < 5; i++) {
            String line = lines[i].trim();
            if (line.contains("|")) {
                String[] columns = line.split("\\|");
                if (columns.length >= 3) {
                    EmotionScore score = new EmotionScore(columns[1].trim(), Integer.parseInt(columns[2].trim().replaceAll("[^\\d]", "")));
                    emotions.add(score);
                }
            }
        }

        String negativeImpactLine = lines[lines.length - 1].replace("|", "").trim();
        return new SentimentAnalysisDto(emotions, negativeImpactLine);
    }
}
