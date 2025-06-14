package com.example.bloombackend.item.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bloombackend.global.config.annotation.CurrentUser;
import com.example.bloombackend.item.controller.dto.response.ItemsResponse;
import com.example.bloombackend.item.controller.dto.response.UserItemsResponse;
import com.example.bloombackend.item.service.ItemService;

@RestController
@RequestMapping("/api/item")
public class ItemController {
	private final ItemService itemService;

	public ItemController(ItemService itemService) {
		this.itemService = itemService;
	}

	@GetMapping("/sale")
	public ResponseEntity<ItemsResponse> getItems() {
		return ResponseEntity.ok(itemService.getOnSaleItems());
	}

	@GetMapping("/inventory")
	public ResponseEntity<UserItemsResponse> getUserItems(
		@CurrentUser Long userId
	) {
		return ResponseEntity.ok(itemService.getUserItems(userId));
	}
}
