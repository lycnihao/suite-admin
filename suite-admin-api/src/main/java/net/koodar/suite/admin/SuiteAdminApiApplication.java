package net.koodar.suite.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@ComponentScan(value = {"net.koodar.suite"})
@SpringBootApplication
public class SuiteAdminApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(SuiteAdminApiApplication.class, args);
    }

}
