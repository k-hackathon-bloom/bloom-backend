package com.example.bloombackend.bottlemsg.repository.querydsl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.bloombackend.bottlemsg.entity.BottleMessageEntity;
import com.example.bloombackend.bottlemsg.entity.Negativity;
import com.example.bloombackend.bottlemsg.entity.QBottleMessageEntity;
import com.example.bloombackend.bottlemsg.entity.QBottleMessageReceiptLog;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
public class BottleMessageRepositoryImpl implements BottleMessageRepositoryCustom {
	private final JPAQueryFactory queryFactory;

	public BottleMessageRepositoryImpl(JPAQueryFactory queryFactory) {
		this.queryFactory = queryFactory;
	}

	@Override
	public List<BottleMessageEntity> findUnreceivedMessagesByUserId(Long userId) {
		QBottleMessageEntity bottleMessage = QBottleMessageEntity.bottleMessageEntity;

		return queryFactory
			.selectFrom(bottleMessage)
			.where(
				isNotReceived(userId),
				isNotSentByUser(userId),
				hasNegativityLevel(Negativity.LOWER)
			)
			.orderBy(randomOrder())
			.limit(10)
			.fetch();
	}

	@Override
	public List<BottleMessageEntity> findSavedMessagesByUserId(Long userId) {
		QBottleMessageEntity bottleMessage = QBottleMessageEntity.bottleMessageEntity;
		QBottleMessageReceiptLog receiptLog = QBottleMessageReceiptLog.bottleMessageReceiptLog;

		return queryFactory
			.selectFrom(bottleMessage)
			.where(bottleMessage.id.in(
				queryFactory.select(receiptLog.message.id)
					.from(receiptLog)
					.where(receiptLog.recipient.id.eq(userId)
						.and(receiptLog.isSaved.eq(true)))
			))
			.fetch();
	}

	private BooleanExpression isNotReceived(Long userId) {
		QBottleMessageReceiptLog receiptLog = QBottleMessageReceiptLog.bottleMessageReceiptLog;
		QBottleMessageEntity bottleMessage = QBottleMessageEntity.bottleMessageEntity;
		return bottleMessage.id.notIn(
			JPAExpressions.select(receiptLog.message.id)
				.from(receiptLog)
				.where(receiptLog.recipient.id.eq(userId))
		);
	}

	private BooleanExpression isNotSentByUser(Long userId) {
		QBottleMessageEntity bottleMessage = QBottleMessageEntity.bottleMessageEntity;
		return bottleMessage.sender.id.ne(userId);
	}

	private BooleanExpression hasNegativityLevel(Negativity level) {
		QBottleMessageEntity bottleMessage = QBottleMessageEntity.bottleMessageEntity;
		return bottleMessage.negativity.eq(level);
	}

	private OrderSpecifier<Double> randomOrder() {
		return Expressions.numberTemplate(Double.class, "function('RAND')").asc();
	}
}
