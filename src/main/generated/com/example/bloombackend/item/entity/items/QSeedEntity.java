package com.example.bloombackend.item.entity.items;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QSeedEntity is a Querydsl query type for SeedEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSeedEntity extends EntityPathBase<SeedEntity> {

    private static final long serialVersionUID = -2120067970L;

    public static final QSeedEntity seedEntity = new QSeedEntity("seedEntity");

    public final com.example.bloombackend.item.entity.QItemEntity _super = new com.example.bloombackend.item.entity.QItemEntity(this);

    public final StringPath bigIconUrl = createString("bigIconUrl");

    //inherited
    public final DatePath<java.time.LocalDate> endDate = _super.endDate;

    //inherited
    public final NumberPath<Long> id = _super.id;

    //inherited
    public final BooleanPath isDefault = _super.isDefault;

    //inherited
    public final StringPath name = _super.name;

    //inherited
    public final NumberPath<Integer> price = _super.price;

    public final StringPath smallIconUrl = createString("smallIconUrl");

    //inherited
    public final StringPath thumbnailImgUrl = _super.thumbnailImgUrl;

    public QSeedEntity(String variable) {
        super(SeedEntity.class, forVariable(variable));
    }

    public QSeedEntity(Path<? extends SeedEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSeedEntity(PathMetadata metadata) {
        super(SeedEntity.class, metadata);
    }

}

