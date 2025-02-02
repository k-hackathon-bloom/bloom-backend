package com.example.bloombackend.credit.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.bloombackend.credit.entity.CreditType;
import com.example.bloombackend.credit.entity.UserCreditEntity;
import com.example.bloombackend.credit.repository.UserCreditRepository;
import com.example.bloombackend.credit.service.dto.UserCreditInfo;
import com.example.bloombackend.user.entity.UserEntity;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CreditService {
	private static final int INIT_BUTTON_CREDIT = 0;
	private static final int INIT_CASH_CREDIT = 0;

	private final UserCreditRepository userCreditRepository;

	public CreditService(UserCreditRepository userCreditRepository) {
		this.userCreditRepository = userCreditRepository;
	}

	@Transactional
	public UserCreditInfo createUserCredit(UserEntity user) {
		return new UserCreditInfo(
			initCredit(user, CreditType.BUTTON, INIT_BUTTON_CREDIT).getBalance(),
			initCredit(user, CreditType.CASH, INIT_CASH_CREDIT).getBalance());
	}

	private UserCreditEntity initCredit(UserEntity user, CreditType creditType, int initialBalance) {
		return userCreditRepository.save(
			UserCreditEntity.builder()
				.creditType(creditType)
				.balance(initialBalance)
				.user(user)
				.build()
		);
	}

	@Transactional(readOnly = true)
	public UserCreditInfo getUserCredit(Long userId) {
		return new UserCreditInfo(getBalance(userId, CreditType.BUTTON), getBalance(userId, CreditType.CASH));
	}

	private int getBalance(Long userId, CreditType creditType) {
		return userCreditRepository.findUserCreditEntityByUserIdAndCreditType(userId, creditType)
			.map(UserCreditEntity::getBalance)
			.orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));
	}
}
