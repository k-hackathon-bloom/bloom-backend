package com.example.bloombackend.item.controller.dto.response.info;

import java.time.LocalDate;

import com.example.bloombackend.item.entity.ItemEntity;

import lombok.Builder;

@Builder
public record ItemInfo(
	Long id,
	String name,
	Integer price,
	String thumbnailUrl,
	String type,
	LocalDate endDate
) {
	public static ItemInfo from(ItemEntity item) {
		return ItemInfo.builder()
			.id(item.getId())
			.name(item.getName())
			.price(item.getPrice())
			.thumbnailUrl(item.getThumbnailImgUrl())
			.type(item.getType())
			.endDate(item.getEndDate()).build();
	}
}
