package com.example.session11;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class Session11RedisCacheApplication {
    public static void main(String[] args) {
        SpringApplication.run(Session11RedisCacheApplication.class, args);
    }
}
