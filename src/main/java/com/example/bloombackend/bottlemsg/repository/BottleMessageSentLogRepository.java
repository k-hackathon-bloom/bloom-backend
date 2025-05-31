package com.example.bloombackend.bottlemsg.repository;

import com.example.bloombackend.bottlemsg.entity.BottleMessageSentLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BottleMessageSentLogRepository extends JpaRepository<BottleMessageSentLog, Long> {
}
