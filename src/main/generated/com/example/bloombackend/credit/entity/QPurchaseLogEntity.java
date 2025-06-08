package com.example.bloombackend.credit.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QPurchaseLogEntity is a Querydsl query type for PurchaseLogEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPurchaseLogEntity extends EntityPathBase<PurchaseLogEntity> {

    private static final long serialVersionUID = 969509316L;

    public static final QPurchaseLogEntity purchaseLogEntity = new QPurchaseLogEntity("purchaseLogEntity");

    public final NumberPath<Integer> amount = createNumber("amount", Integer.class);

    public final NumberPath<Integer> balance = createNumber("balance", Integer.class);

    public final EnumPath<CreditType> creditType = createEnum("creditType", CreditType.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> itemId = createNumber("itemId", Long.class);

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QPurchaseLogEntity(String variable) {
        super(PurchaseLogEntity.class, forVariable(variable));
    }

    public QPurchaseLogEntity(Path<? extends PurchaseLogEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPurchaseLogEntity(PathMetadata metadata) {
        super(PurchaseLogEntity.class, metadata);
    }

}

