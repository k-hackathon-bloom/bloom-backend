package com.example.bloombackend.oauth.repository;

import com.example.bloombackend.oauth.entity.LogoutList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface LogoutListRepository extends JpaRepository<LogoutList, Long> {
    Optional<LogoutList> findByTokenHash(String tokenHash);
    void deleteByExpirationTimeBefore(LocalDateTime now);
}