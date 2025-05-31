package com.example.bloombackend.bottlemsg.repository;

import com.example.bloombackend.bottlemsg.entity.BottleMessageSentLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BottleMessageSentLogRepository extends JpaRepository<BottleMessageSentLog, Long> {
    Optional<BottleMessageSentLog> findByMessageId(Long messageId);
    List<BottleMessageSentLog> findBySenderIdAndIsHide(Long senderId, boolean isHide);

}
