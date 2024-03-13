package guard.passer.core.config;

import guard.passer.core.database.pool.ConnectionPool;
import guard.passer.core.database.reporitory.UserRepository;
import guard.passer.web.config.WebConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.*;

@Import(WebConfiguration.class)
@Configuration
//@PropertySource("classpath:application.properties") // автоматом есть в  boot
//@ComponentScan(basePackages = "guard.passer.core") // автоматом есть в  boot
@ConfigurationPropertiesScan //для скана DatabaseProperties
public class ApplicationConfiguration {

    @Bean
    public ConnectionPool pool2(@Value("${db.username}") String username){
        return new ConnectionPool(username, 7);
    }

    @Bean
    public ConnectionPool pool3(){
        return new ConnectionPool("test3", 20);
    }

//    @Bean("User Repo2")
//    @Profile("prod|dev")
////    ! & |
//    public UserRepository userRepository2(ConnectionPool pool2){
//        return new UserRepository(pool2);
//    }
//
//    @Bean("User Repo3")
//    public UserRepository userRepository3(){
//        return new UserRepository(pool3());
//    }
}
