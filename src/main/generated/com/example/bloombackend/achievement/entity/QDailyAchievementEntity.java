package com.example.bloombackend.achievement.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QDailyAchievementEntity is a Querydsl query type for DailyAchievementEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDailyAchievementEntity extends EntityPathBase<DailyAchievementEntity> {

    private static final long serialVersionUID = 546590585L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QDailyAchievementEntity dailyAchievementEntity = new QDailyAchievementEntity("dailyAchievementEntity");

    public final NumberPath<Integer> achievementLevel = createNumber("achievementLevel", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final com.example.bloombackend.item.entity.items.QSeedEntity flower;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.example.bloombackend.user.entity.QUserEntity user;

    public QDailyAchievementEntity(String variable) {
        this(DailyAchievementEntity.class, forVariable(variable), INITS);
    }

    public QDailyAchievementEntity(Path<? extends DailyAchievementEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QDailyAchievementEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QDailyAchievementEntity(PathMetadata metadata, PathInits inits) {
        this(DailyAchievementEntity.class, metadata, inits);
    }

    public QDailyAchievementEntity(Class<? extends DailyAchievementEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.flower = inits.isInitialized("flower") ? new com.example.bloombackend.item.entity.items.QSeedEntity(forProperty("flower")) : null;
        this.user = inits.isInitialized("user") ? new com.example.bloombackend.user.entity.QUserEntity(forProperty("user")) : null;
    }

}

