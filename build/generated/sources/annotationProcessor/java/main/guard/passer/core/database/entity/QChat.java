package guard.passer.core.database.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QChat is a Querydsl query type for Chat
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QChat extends EntityPathBase<Chat> {

    private static final long serialVersionUID = 633152088L;

    public static final QChat chat = new QChat("chat");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final ListPath<UserChat, QUserChat> userChats = this.<UserChat, QUserChat>createList("userChats", UserChat.class, QUserChat.class, PathInits.DIRECT2);

    public QChat(String variable) {
        super(Chat.class, forVariable(variable));
    }

    public QChat(Path<? extends Chat> path) {
        super(path.getType(), path.getMetadata());
    }

    public QChat(PathMetadata metadata) {
        super(Chat.class, metadata);
    }

}

