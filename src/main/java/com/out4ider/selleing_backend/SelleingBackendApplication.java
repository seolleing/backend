package com.out4ider.selleing_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class SelleingBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(SelleingBackendApplication.class, args);
    }

}
