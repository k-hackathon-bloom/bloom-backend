package com.example.bloombackend.item.controller.dto.response;

import java.util.List;

import com.example.bloombackend.item.controller.dto.response.info.ItemInfo;

public record UserItemsResponse(
	List<ItemInfo> defaultItems,
	List<ItemInfo> purchasedItems
) {
}
