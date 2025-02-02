package com.example.bloombackend.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.bloombackend.item.entity.items.SeedEntity;

@Repository("SeedRepository")
public interface SeedRepository extends JpaRepository<SeedEntity, Long> {
}
