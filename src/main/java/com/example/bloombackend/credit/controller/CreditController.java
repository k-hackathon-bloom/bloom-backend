package com.example.bloombackend.credit.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bloombackend.credit.controller.dto.request.PurchaseRequest;
import com.example.bloombackend.credit.controller.dto.response.PurchaseResponse;
import com.example.bloombackend.credit.service.CreditService;
import com.example.bloombackend.global.config.annotation.CurrentUser;

@RestController
@RequestMapping("/api/credit")
public class CreditController {
	private final CreditService creditService;

	public CreditController(CreditService creditService) {
		this.creditService = creditService;
	}

	@PostMapping("/purchase")
	public ResponseEntity<PurchaseResponse> purchase(
		@CurrentUser Long userId,
		@RequestBody PurchaseRequest purchaseRequest) {
		return ResponseEntity.ok(creditService.purchaseItem(userId, purchaseRequest));
	}
}
