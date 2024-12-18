package com.example.bloombackend.item.service;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.bloombackend.item.controller.dto.response.ItemsResponse;
import com.example.bloombackend.item.controller.dto.response.UserItemsResponse;
import com.example.bloombackend.item.controller.dto.response.info.ItemInfo;
import com.example.bloombackend.item.entity.ItemEntity;
import com.example.bloombackend.item.entity.UserItemEntity;
import com.example.bloombackend.item.repository.ItemRepository;
import com.example.bloombackend.item.repository.UserItemRepository;

@Service
public class ItemService {
	private static final boolean IS_DEFAULT = true;

	private final ItemRepository itemRepository;
	private final UserItemRepository userItemRepository;

	public ItemService(ItemRepository itemRepository, UserItemRepository userItemRepository) {
		this.itemRepository = itemRepository;
		this.userItemRepository = userItemRepository;
	}

	@Transactional(readOnly = true)
	public ItemsResponse getOnSaleItems() {
		List<ItemEntity> items = itemRepository.findOnSaleItems();
		return new ItemsResponse(
			items.stream()
				.map(ItemInfo::from)
				.toList()
		);
	}

	@Transactional(readOnly = true)
	public UserItemsResponse getUserItems(Long userId) {
		return new UserItemsResponse(getDefaultItems(), getPurchasedItems(userId));
	}

	@Cacheable("defaultItems")
	public List<ItemInfo> getDefaultItems() {
		List<ItemEntity> items = itemRepository.findByIsDefault(IS_DEFAULT);
		return items.stream()
			.map(ItemInfo::from)
			.toList();
	}

	private List<ItemInfo> getPurchasedItems(Long userId) {
		List<UserItemEntity> items = userItemRepository.findByUserId(userId);
		return items.stream()
			.map(UserItemEntity::getItem)
			.map(ItemInfo::from)
			.toList();
	}
}
