package com.shortner.url;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableKafka
@EnableScheduling
public class UrlApplication {

    public static void main(String[] args) {
        SpringApplication.run(UrlApplication.class, args);
        System.out.println("hi");
    }

}
