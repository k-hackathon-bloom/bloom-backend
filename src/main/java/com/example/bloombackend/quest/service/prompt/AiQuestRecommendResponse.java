package com.example.bloombackend.quest.service.prompt;

import java.util.List;

public record AiQuestRecommendResponse(List<Long> recommendedQuestIds) {
}