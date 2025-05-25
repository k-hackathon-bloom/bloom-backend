package com.example.bloombackend.bottlemsg.service.dto;

import java.util.List;

public record SentimentAnalysisDto(List<EmotionScore> emotions, String negativeImpact) {
}
