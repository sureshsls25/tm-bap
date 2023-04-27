package com.ms.bap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableJpaRepositories
@EnableWebMvc
//@EnableCaching
public class TMBAPApplication {

    public static void main(String[] args) {
        SpringApplication.run(TMBAPApplication.class, args);
    }

}
