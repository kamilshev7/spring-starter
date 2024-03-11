package guard.passer.core.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;


@ConfigurationProperties(prefix = "db")
public record DatabaseProperties(String username,
                                 String password,
                                 String driver,
                                 String url,
                                 List<String> hosts,
                                 PoolProperties pool) {

    public record PoolProperties(Integer size,
                                 Integer timeout) {

    }
}
