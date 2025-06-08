package com.example.bloombackend.bottlemsg.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.bloombackend.bottlemsg.entity.BottleMessageEntity;
import com.example.bloombackend.bottlemsg.repository.querydsl.BottleMessageRepositoryCustom;
import com.example.bloombackend.user.entity.UserEntity;

public interface BottleMessageRepository
	extends JpaRepository<BottleMessageEntity, Long>, BottleMessageRepositoryCustom {
	List<BottleMessageEntity> findBySenderId(Long userId);

	Optional<BottleMessageEntity> findTopBySenderIdOrderByCreatedAtDesc(Long userId);
}
