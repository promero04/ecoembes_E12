package com.ecoembes.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = "com.ecoembes")
@EnableJpaRepositories(basePackages = "com.ecoembes.repository")
@EntityScan(basePackages = "com.ecoembes.entity")
@EnableTransactionManagement
public class EcoembesE12App {

	public static void main(String[] args) {
		SpringApplication.run(EcoembesE12App.class, args);
	}

}
