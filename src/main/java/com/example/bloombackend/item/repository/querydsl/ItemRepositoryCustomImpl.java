package com.example.bloombackend.item.repository.querydsl;

import java.time.LocalDate;
import java.util.List;

import com.example.bloombackend.item.entity.ItemEntity;
import com.example.bloombackend.item.entity.QItemEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;

public class ItemRepositoryCustomImpl implements ItemRepositoryCustom {
	private final JPAQueryFactory queryFactory;

	public ItemRepositoryCustomImpl(JPAQueryFactory queryFactory) {
		this.queryFactory = queryFactory;
	}

	@Override
	public List<ItemEntity> findOnSaleItems() {
		QItemEntity item = QItemEntity.itemEntity;

		return queryFactory.selectFrom(item)
			.where(
				item.isDefault.eq(false),
				item.endDate.goe(LocalDate.now())
			)
			.fetch();
	}
}
