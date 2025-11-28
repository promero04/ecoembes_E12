package com.plassb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.plassb")
public class PlasSbApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(PlasSbApplication.class);
        app.setAdditionalProfiles("plassb");
        app.run(args);
    }
}
