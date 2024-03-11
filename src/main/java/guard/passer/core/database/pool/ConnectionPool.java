package guard.passer.core.database.pool;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Slf4j
@Component(value = "pool1")
@RequiredArgsConstructor
public class ConnectionPool {
    @Value("${db.username}")
    private final String username;
    @Value("${db.pool.size}")
    private final Integer poolSize;


    @PostConstruct
    private void myInit() {
        log.info("*** ---init Connection pool " + this);
    }

    @PreDestroy
    private void myDestroy() {
        log.info("*** ---close Connection pool " + this);
    }
}
