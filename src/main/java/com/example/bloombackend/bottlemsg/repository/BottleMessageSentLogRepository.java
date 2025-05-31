package com.example.bloombackend.bottlemsg.repository;

import com.example.bloombackend.bottlemsg.entity.BottleMessageSentLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BottleMessageSentLogRepository extends JpaRepository<BottleMessageSentLog, Long> {
    List<BottleMessageSentLog> findBySenderIdAndIsSaved(Long senderId, boolean isSaved);
}
