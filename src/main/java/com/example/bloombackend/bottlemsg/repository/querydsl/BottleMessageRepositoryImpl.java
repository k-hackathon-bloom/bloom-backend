package com.example.bloombackend.bottlemsg.repository.querydsl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import com.example.bloombackend.bottlemsg.entity.*;
import org.springframework.stereotype.Repository;

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

	@Override
	public Optional<BottleMessageSentLog> findTodayLowerMessage(Long senderId) {
		QBottleMessageSentLog sentLog = QBottleMessageSentLog.bottleMessageSentLog;
		QBottleMessageEntity message = QBottleMessageEntity.bottleMessageEntity;

		LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
		LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX);

		return Optional.ofNullable(queryFactory
				.selectFrom(sentLog)
				.join(sentLog.message, message)
				.where(
						sentLog.senderId.eq(senderId),
						sentLog.isHide.eq(false), // 필요 시
						message.createdAt.between(startOfDay, endOfDay),
						message.negativity.eq(Negativity.LOWER)
				)
				.orderBy(message.createdAt.desc())
				.limit(1)
				.fetchOne());
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
