package guard.passer.core;

import guard.passer.core.service.CompanyService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class ApplicationRunner {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(ApplicationRunner.class, args);
        CompanyService companyService = context.getBean("companyService", CompanyService.class);
        companyService.findById(1);
        System.out.println();
    }
}
