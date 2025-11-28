package com.contsocket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.contsocket")
public class ContSocketApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(ContSocketApplication.class);
        app.setAdditionalProfiles("contsocket");
        app.run(args);
    }
}
