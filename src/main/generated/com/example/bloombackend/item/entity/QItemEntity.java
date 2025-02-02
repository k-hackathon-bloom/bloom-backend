package com.example.bloombackend.item.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QItemEntity is a Querydsl query type for ItemEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QItemEntity extends EntityPathBase<ItemEntity> {

    private static final long serialVersionUID = 783752626L;

    public static final QItemEntity itemEntity = new QItemEntity("itemEntity");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath imgUrl = createString("imgUrl");

    public final BooleanPath isSale = createBoolean("isSale");

    public final StringPath name = createString("name");

    public final NumberPath<Integer> price = createNumber("price", Integer.class);

    public final EnumPath<ItemType> type = createEnum("type", ItemType.class);

    public QItemEntity(String variable) {
        super(ItemEntity.class, forVariable(variable));
    }

    public QItemEntity(Path<? extends ItemEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QItemEntity(PathMetadata metadata) {
        super(ItemEntity.class, metadata);
    }

}

