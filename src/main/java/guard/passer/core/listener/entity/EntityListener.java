package guard.passer.core.listener.entity;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EntityListener {

    @EventListener(condition = "#p0.accessType.name() == 'READ'")
    public void acceptEntity(EntityEvent entityEvent){
        log.info("*** Event: " + entityEvent);
    }
}
