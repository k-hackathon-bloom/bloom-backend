package com.example.bloombackend.quest.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserQuestLogEntity is a Querydsl query type for UserQuestLogEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserQuestLogEntity extends EntityPathBase<UserQuestLogEntity> {

    private static final long serialVersionUID = 66811331L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserQuestLogEntity userQuestLogEntity = new QUserQuestLogEntity("userQuestLogEntity");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isDone = createBoolean("isDone");

    public final QQuestEntity quest;

    public final DateTimePath<java.time.LocalDateTime> selectedDate = createDateTime("selectedDate", java.time.LocalDateTime.class);

    public final com.example.bloombackend.user.entity.QUserEntity user;

    public QUserQuestLogEntity(String variable) {
        this(UserQuestLogEntity.class, forVariable(variable), INITS);
    }

    public QUserQuestLogEntity(Path<? extends UserQuestLogEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserQuestLogEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserQuestLogEntity(PathMetadata metadata, PathInits inits) {
        this(UserQuestLogEntity.class, metadata, inits);
    }

    public QUserQuestLogEntity(Class<? extends UserQuestLogEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.quest = inits.isInitialized("quest") ? new QQuestEntity(forProperty("quest")) : null;
        this.user = inits.isInitialized("user") ? new com.example.bloombackend.user.entity.QUserEntity(forProperty("user")) : null;
    }

}

