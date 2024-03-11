package guard.passer.core.listener.entity;

import lombok.Getter;

import java.util.EventObject;

public class EntityEvent extends EventObject {

    @Getter
    private final AccessType accessType;

    public EntityEvent(Object source, AccessType accessType) {
        super(source);
        this.accessType = accessType;
    }

}
