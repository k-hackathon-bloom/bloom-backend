package com.example.bloombackend.quest.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QQuestEntity is a Querydsl query type for QuestEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QQuestEntity extends EntityPathBase<QuestEntity> {

    private static final long serialVersionUID = -2012651726L;

    public static final QQuestEntity questEntity = new QQuestEntity("questEntity");

    public final StringPath iconUrl = createString("iconUrl");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> maxCount = createNumber("maxCount", Integer.class);

    public final StringPath title = createString("title");

    public QQuestEntity(String variable) {
        super(QuestEntity.class, forVariable(variable));
    }

    public QQuestEntity(Path<? extends QuestEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QQuestEntity(PathMetadata metadata) {
        super(QuestEntity.class, metadata);
    }

}

