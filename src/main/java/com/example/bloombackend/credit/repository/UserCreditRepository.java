package com.example.bloombackend.credit.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.bloombackend.credit.entity.CreditType;
import com.example.bloombackend.credit.entity.UserCreditEntity;

public interface UserCreditRepository extends JpaRepository<UserCreditEntity, Long> {
	Optional<UserCreditEntity> findUserCreditEntityByUserIdAndCreditType(Long userId, CreditType creditType);
}
