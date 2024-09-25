package com.example.bloombackend.achievement.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QFlowerEntity is a Querydsl query type for FlowerEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFlowerEntity extends EntityPathBase<FlowerEntity> {

    private static final long serialVersionUID = -1091552578L;

    public static final QFlowerEntity flowerEntity = new QFlowerEntity("flowerEntity");

    public final StringPath description = createString("description");

    public final StringPath iconUrl = createString("iconUrl");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public QFlowerEntity(String variable) {
        super(FlowerEntity.class, forVariable(variable));
    }

    public QFlowerEntity(Path<? extends FlowerEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFlowerEntity(PathMetadata metadata) {
        super(FlowerEntity.class, metadata);
    }

}

