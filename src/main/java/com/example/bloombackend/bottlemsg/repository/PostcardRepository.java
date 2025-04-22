package com.example.bloombackend.bottlemsg.repository;

import com.example.bloombackend.bottlemsg.entity.PostcardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostcardRepository extends JpaRepository<PostcardEntity,Long> {
}
