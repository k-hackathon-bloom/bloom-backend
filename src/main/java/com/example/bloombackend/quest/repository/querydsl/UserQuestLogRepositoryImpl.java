package com.example.bloombackend.quest.repository.querydsl;

import com.example.bloombackend.quest.entity.UserQuestLogEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.example.bloombackend.quest.entity.QUserQuestLogEntity.userQuestLogEntity;

public class UserQuestLogRepositoryImpl implements UserQuestLogRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public UserQuestLogRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public void completeQuest(Long userId, Long questId) {
        queryFactory.update(userQuestLogEntity)
                .set(userQuestLogEntity.isDone, true)
                .where(userQuestLogEntity.user.id.eq(userId)
                        .and(userQuestLogEntity.quest.id.eq(questId)))
                .execute();
    }

    @Override
    public List<UserQuestLogEntity> findIncompleteQuestsByDate(LocalDate selectedDate) {
        LocalDateTime startOfDay = selectedDate.atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1).minusSeconds(1);
        return queryFactory.selectFrom(userQuestLogEntity)
                .where(userQuestLogEntity.selectedDate.between(startOfDay, endOfDay)
                        .and(userQuestLogEntity.isDone.eq(false)))
                .fetch();
    }
}