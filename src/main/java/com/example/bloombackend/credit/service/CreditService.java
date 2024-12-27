package com.example.bloombackend.credit.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.bloombackend.credit.controller.dto.request.PurchaseRequest;
import com.example.bloombackend.credit.controller.dto.response.PurchaseResponse;
import com.example.bloombackend.credit.entity.CreditType;
import com.example.bloombackend.credit.entity.PurchaseLogEntity;
import com.example.bloombackend.credit.entity.UserCreditEntity;
import com.example.bloombackend.credit.repository.PurchaseLogRepository;
import com.example.bloombackend.credit.repository.UserCreditRepository;
import com.example.bloombackend.credit.service.dto.UserCreditInfo;
import com.example.bloombackend.item.controller.dto.response.info.ItemInfo;
import com.example.bloombackend.item.service.ItemService;
import com.example.bloombackend.user.entity.UserEntity;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CreditService {
	private static final int INIT_BUTTON_CREDIT = 0;
	private static final int INIT_CASH_CREDIT = 0;

	private final UserCreditRepository userCreditRepository;
	private final PurchaseLogRepository purchaseLogRepository;
	private final ItemService itemService;

	public CreditService(UserCreditRepository userCreditRepository, PurchaseLogRepository purchaseLogRepository,
		ItemService itemService) {
		this.userCreditRepository = userCreditRepository;
		this.purchaseLogRepository = purchaseLogRepository;
		this.itemService = itemService;
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

	@Transactional
	public PurchaseResponse purchaseItem(Long userId, PurchaseRequest request) {
		ItemInfo purchasedItem = itemService.addUserItem(userId, request.itemId());
		int balance = updateUserCredit(userId, -purchasedItem.price(), request.creditType());
		createPurchaseLog(userId, purchasedItem, request.creditType(), balance);
		return new PurchaseResponse(purchasedItem, getUserCredit(userId));
	}

	private int updateUserCredit(Long userId, int amount, CreditType creditType) {
		UserCreditEntity userCredit = userCreditRepository.findUserCreditEntityByUserIdAndCreditType(userId, creditType)
			.orElseThrow(() -> new EntityNotFoundException("UserItem not found"));
		return userCredit.updateBalance(amount);
	}

	private void createPurchaseLog(Long userId, ItemInfo item, CreditType creditType, int balance) {
		purchaseLogRepository.save(
			PurchaseLogEntity.builder()
				.userId(userId)
				.itemId(item.id())
				.amount(item.price())
				.balance(balance)
				.creditType(creditType)
				.build()
		);
	}
}
