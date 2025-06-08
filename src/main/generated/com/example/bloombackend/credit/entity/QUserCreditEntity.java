package com.example.bloombackend.credit.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserCreditEntity is a Querydsl query type for UserCreditEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserCreditEntity extends EntityPathBase<UserCreditEntity> {

    private static final long serialVersionUID = 1485991529L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserCreditEntity userCreditEntity = new QUserCreditEntity("userCreditEntity");

    public final NumberPath<Integer> balance = createNumber("balance", Integer.class);

    public final EnumPath<CreditType> creditType = createEnum("creditType", CreditType.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.example.bloombackend.user.entity.QUserEntity user;

    public QUserCreditEntity(String variable) {
        this(UserCreditEntity.class, forVariable(variable), INITS);
    }

    public QUserCreditEntity(Path<? extends UserCreditEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserCreditEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserCreditEntity(PathMetadata metadata, PathInits inits) {
        this(UserCreditEntity.class, metadata, inits);
    }

    public QUserCreditEntity(Class<? extends UserCreditEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new com.example.bloombackend.user.entity.QUserEntity(forProperty("user")) : null;
    }

}

