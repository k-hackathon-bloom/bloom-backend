package com.example.bloombackend.quest.controller.dto;

import java.util.List;

public record QuestRecommendResponse(List<Long> recommendedQuestIds) {
}