package com.example.bloombackend.quest.service.prompt;

import java.time.LocalDateTime;

public record UserQuestLogForPrompt(Long questId, String questTitle, LocalDateTime selectedDate, boolean isDone) {
}