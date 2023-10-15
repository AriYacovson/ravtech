package com.example.ravtech;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableRetry
public class RavtechApplication {

    public static void main(String[] args) {
        SpringApplication.run(RavtechApplication.class, args);
    }

}
