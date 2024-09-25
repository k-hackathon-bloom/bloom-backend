package com.example.bloombackend.donelist.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QDoneList is a Querydsl query type for DoneList
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDoneList extends EntityPathBase<DoneList> {

    private static final long serialVersionUID = 259105577L;

    public static final QDoneList doneList = new QDoneList("doneList");

    public final StringPath content = createString("content");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final DatePath<java.time.LocalDate> doneDate = createDate("doneDate", java.time.LocalDate.class);

    public final StringPath iconUrl = createString("iconUrl");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath title = createString("title");

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QDoneList(String variable) {
        super(DoneList.class, forVariable(variable));
    }

    public QDoneList(Path<? extends DoneList> path) {
        super(path.getType(), path.getMetadata());
    }

    public QDoneList(PathMetadata metadata) {
        super(DoneList.class, metadata);
    }

}

