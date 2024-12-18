package com.example.bloombackend.item.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.bloombackend.item.entity.ItemEntity;
import com.example.bloombackend.item.repository.querydsl.ItemRepositoryCustom;

public interface ItemRepository extends JpaRepository<ItemEntity, Long>, ItemRepositoryCustom {
	List<ItemEntity> findByIsDefault(boolean isDefault);
}
