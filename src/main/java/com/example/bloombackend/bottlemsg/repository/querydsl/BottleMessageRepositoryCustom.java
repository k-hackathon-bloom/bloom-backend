package com.example.bloombackend.bottlemsg.repository.querydsl;

import java.util.List;
import java.util.Optional;

import com.example.bloombackend.bottlemsg.entity.BottleMessageEntity;
import com.example.bloombackend.bottlemsg.entity.BottleMessageSentLog;

public interface BottleMessageRepositoryCustom {
	List<BottleMessageEntity> findUnreceivedMessagesByUserId(Long userId);
	Optional<BottleMessageSentLog> findTodayLowerMessage(Long senderId);
	List<BottleMessageEntity> findSavedMessagesByUserId(Long userId);
}
