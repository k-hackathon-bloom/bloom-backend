package com.example.bloombackend.item.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.bloombackend.item.controller.dto.response.ItemsResponse;
import com.example.bloombackend.item.controller.dto.response.info.ItemInfo;
import com.example.bloombackend.item.entity.ItemEntity;
import com.example.bloombackend.item.repository.ItemRepository;

@Service
public class ItemService {
	private static final boolean IS_ON_SALE = true;

	private final ItemRepository itemRepository;

	public ItemService(ItemRepository itemRepository) {
		this.itemRepository = itemRepository;
	}

	public ItemsResponse getItems() {
		List<ItemEntity> items = itemRepository.findByIsSale(IS_ON_SALE);
		return new ItemsResponse(
			items.stream()
				.map(ItemInfo::from)
				.toList()
		);
	}
}
