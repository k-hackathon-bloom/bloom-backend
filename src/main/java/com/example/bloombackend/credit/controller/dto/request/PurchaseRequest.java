package com.example.bloombackend.credit.controller.dto.request;

import com.example.bloombackend.credit.entity.CreditType;

public record PurchaseRequest(
	Long itemId,
	CreditType creditType
) {
}
