package com.example.bloombackend.quote.repository;

import com.example.bloombackend.quote.entity.QuoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuoteRepository extends JpaRepository<QuoteEntity, Long> {
}