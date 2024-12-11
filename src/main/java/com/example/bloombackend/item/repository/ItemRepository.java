package com.example.bloombackend.item.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.bloombackend.item.entity.ItemEntity;

public interface ItemRepository extends JpaRepository<ItemEntity, Long> {
	List<ItemEntity> findByIsSale(Boolean isSale);
}
