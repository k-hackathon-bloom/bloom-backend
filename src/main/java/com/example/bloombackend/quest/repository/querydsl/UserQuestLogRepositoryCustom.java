package com.example.bloombackend.quest.repository.querydsl;

import com.example.bloombackend.quest.entity.UserQuestLogEntity;

import java.time.LocalDate;
import java.util.List;

public interface UserQuestLogRepositoryCustom {
    void completeQuest(Long userId, Long questId);

    public List<UserQuestLogEntity> findIncompleteQuestsByDate(LocalDate selectedDate);
}