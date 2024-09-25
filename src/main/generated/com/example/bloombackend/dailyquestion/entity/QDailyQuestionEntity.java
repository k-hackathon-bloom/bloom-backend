package com.example.bloombackend.dailyquestion.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QDailyQuestionEntity is a Querydsl query type for DailyQuestionEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDailyQuestionEntity extends EntityPathBase<DailyQuestionEntity> {

    private static final long serialVersionUID = 1381625458L;

    public static final QDailyQuestionEntity dailyQuestionEntity = new QDailyQuestionEntity("dailyQuestionEntity");

    public final StringPath content = createString("content");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public QDailyQuestionEntity(String variable) {
        super(DailyQuestionEntity.class, forVariable(variable));
    }

    public QDailyQuestionEntity(Path<? extends DailyQuestionEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QDailyQuestionEntity(PathMetadata metadata) {
        super(DailyQuestionEntity.class, metadata);
    }

}

