package net.koodar.suite.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@ComponentScan(value = {"net.koodar.suite"})
@EntityScan(basePackages = "net.koodar.suite")
@EnableJpaRepositories(basePackages = "net.koodar.suite")
@EnableAspectJAutoProxy
@SpringBootApplication
public class SuiteAdminApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(SuiteAdminApiApplication.class, args);
    }

}
