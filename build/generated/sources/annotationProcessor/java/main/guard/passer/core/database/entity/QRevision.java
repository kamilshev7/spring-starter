package guard.passer.core.database.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QRevision is a Querydsl query type for Revision
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRevision extends EntityPathBase<Revision> {

    private static final long serialVersionUID = 2116273883L;

    public static final QRevision revision = new QRevision("revision");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final NumberPath<Long> timestamp = createNumber("timestamp", Long.class);

    public QRevision(String variable) {
        super(Revision.class, forVariable(variable));
    }

    public QRevision(Path<? extends Revision> path) {
        super(path.getType(), path.getMetadata());
    }

    public QRevision(PathMetadata metadata) {
        super(Revision.class, metadata);
    }

}

