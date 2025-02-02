package com.example.bloombackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BloomBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BloomBackendApplication.class, args);
    }

}
