package com.example.bloombackend.dailyquestion.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QDailyQuestionLogEntity is a Querydsl query type for DailyQuestionLogEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDailyQuestionLogEntity extends EntityPathBase<DailyQuestionLogEntity> {

    private static final long serialVersionUID = -675895464L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QDailyQuestionLogEntity dailyQuestionLogEntity = new QDailyQuestionLogEntity("dailyQuestionLogEntity");

    public final StringPath answer = createString("answer");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final QDailyQuestionEntity dailyQuestion;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.example.bloombackend.user.entity.QUserEntity user;

    public QDailyQuestionLogEntity(String variable) {
        this(DailyQuestionLogEntity.class, forVariable(variable), INITS);
    }

    public QDailyQuestionLogEntity(Path<? extends DailyQuestionLogEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QDailyQuestionLogEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QDailyQuestionLogEntity(PathMetadata metadata, PathInits inits) {
        this(DailyQuestionLogEntity.class, metadata, inits);
    }

    public QDailyQuestionLogEntity(Class<? extends DailyQuestionLogEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.dailyQuestion = inits.isInitialized("dailyQuestion") ? new QDailyQuestionEntity(forProperty("dailyQuestion")) : null;
        this.user = inits.isInitialized("user") ? new com.example.bloombackend.user.entity.QUserEntity(forProperty("user")) : null;
    }

}

