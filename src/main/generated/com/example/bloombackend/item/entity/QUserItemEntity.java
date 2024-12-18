package com.example.bloombackend.item.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserItemEntity is a Querydsl query type for UserItemEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserItemEntity extends EntityPathBase<UserItemEntity> {

    private static final long serialVersionUID = -443148835L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserItemEntity userItemEntity = new QUserItemEntity("userItemEntity");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QItemEntity item;

    public final com.example.bloombackend.user.entity.QUserEntity user;

    public QUserItemEntity(String variable) {
        this(UserItemEntity.class, forVariable(variable), INITS);
    }

    public QUserItemEntity(Path<? extends UserItemEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserItemEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserItemEntity(PathMetadata metadata, PathInits inits) {
        this(UserItemEntity.class, metadata, inits);
    }

    public QUserItemEntity(Class<? extends UserItemEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.item = inits.isInitialized("item") ? new QItemEntity(forProperty("item")) : null;
        this.user = inits.isInitialized("user") ? new com.example.bloombackend.user.entity.QUserEntity(forProperty("user")) : null;
    }

}

