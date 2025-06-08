package com.example.bloombackend.item.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.bloombackend.item.entity.UserItemEntity;

public interface UserItemRepository extends JpaRepository<UserItemEntity, Long> {
	List<UserItemEntity> findByUserId(Long id);
}
