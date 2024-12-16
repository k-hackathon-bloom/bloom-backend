package com.example.bloombackend.item.repository.querydsl;

import java.util.List;

import com.example.bloombackend.item.entity.ItemEntity;

public interface ItemRepositoryCustom {
	List<ItemEntity> findOnSaleItems();
}
