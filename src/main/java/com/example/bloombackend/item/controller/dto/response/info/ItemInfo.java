package com.example.bloombackend.item.controller.dto.response.info;

import com.example.bloombackend.item.entity.ItemEntity;
import com.example.bloombackend.item.entity.ItemType;

import lombok.Builder;

@Builder
public record ItemInfo(
	Long id,
	String name,
	Integer price,
	String imgUrl,
	ItemType type
) {
	public static ItemInfo from(ItemEntity item) {
		return ItemInfo.builder()
			.id(item.getId())
			.name(item.getName())
			.price(item.getPrice())
			.imgUrl(item.getImgUrl())
			.type(item.getType()).build();
	}
}
