package com.example.bloombackend.user.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import static com.example.bloombackend.user.entity.QUserEntity.userEntity;

public class UserRepositoryImpl implements UserRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public UserRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public void updateFcmToken(Long userId, String token) {
        queryFactory.update(userEntity)
                .where(userEntity.id.eq(userId))
                .set(userEntity.fcmToken, token)
                .execute();
    }
}