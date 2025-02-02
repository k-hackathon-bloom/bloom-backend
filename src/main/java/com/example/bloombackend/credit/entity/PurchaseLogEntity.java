package com.example.bloombackend.credit.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "purchase_log")
public class PurchaseLogEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "purchase_log_id")
	private Long id;

	@Column
	private Long userId;

	@Column(name = "item_id")
	private Long itemId;

	@Enumerated(EnumType.STRING)
	@Column(name = "credit_type")
	private CreditType creditType;

	@Column(name = "amount")
	private int amount;

	@Column(name = "balance")
	private int balance;

	@Builder
	public PurchaseLogEntity(Long userId, Long itemId, CreditType creditType, int amount, int balance) {
		this.userId = userId;
		this.itemId = itemId;
		this.creditType = creditType;
		this.amount = amount;
		this.balance = balance;
	}
}
