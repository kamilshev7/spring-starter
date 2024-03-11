package guard.passer.core.database.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QAuditingEntity is a Querydsl query type for AuditingEntity
 */
@Generated("com.querydsl.codegen.DefaultSupertypeSerializer")
public class QAuditingEntity extends EntityPathBase<AuditingEntity<? extends java.io.Serializable>> {

    private static final long serialVersionUID = 1589891050L;

    public static final QAuditingEntity auditingEntity = new QAuditingEntity("auditingEntity");

    public final DateTimePath<java.time.Instant> createdAt = createDateTime("createdAt", java.time.Instant.class);

    public final StringPath createdBy = createString("createdBy");

    public final DateTimePath<java.time.Instant> modifiedAt = createDateTime("modifiedAt", java.time.Instant.class);

    public final StringPath modifiedBy = createString("modifiedBy");

    @SuppressWarnings({"all", "rawtypes", "unchecked"})
    public QAuditingEntity(String variable) {
        super((Class) AuditingEntity.class, forVariable(variable));
    }

    @SuppressWarnings({"all", "rawtypes", "unchecked"})
    public QAuditingEntity(Path<? extends AuditingEntity> path) {
        super((Class) path.getType(), path.getMetadata());
    }

    @SuppressWarnings({"all", "rawtypes", "unchecked"})
    public QAuditingEntity(PathMetadata metadata) {
        super((Class) AuditingEntity.class, metadata);
    }

}

