package com.example.bloombackend.credit.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.bloombackend.credit.entity.PurchaseLogEntity;

public interface PurchaseLogRepository extends JpaRepository<PurchaseLogEntity, Long> {
}
