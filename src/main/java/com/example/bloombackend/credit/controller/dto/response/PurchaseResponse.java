package com.example.bloombackend.credit.controller.dto.response;

import com.example.bloombackend.credit.service.dto.UserCreditInfo;
import com.example.bloombackend.item.controller.dto.response.info.ItemInfo;

public record PurchaseResponse(
	ItemInfo purchasedItem,
	UserCreditInfo balance
) {
}
