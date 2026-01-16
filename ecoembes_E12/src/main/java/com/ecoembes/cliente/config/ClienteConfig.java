package com.ecoembes.cliente.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@EnableConfigurationProperties(ClienteProperties.class)
@Profile("client")
public class ClienteConfig {

    @Bean
    public WebClient ecoembesWebClient(WebClient.Builder builder, ClienteProperties properties) {
        return builder.baseUrl(properties.getBaseUrl()).build();
    }
}
