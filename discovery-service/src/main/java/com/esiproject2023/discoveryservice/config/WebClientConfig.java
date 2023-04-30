package com.esiproject2023.discoveryservice.config;

import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@LoadBalancerClient
public class WebClientConfig {
    @Bean
    public WebClient.Builder webClient() {
        return WebClient.builder();
    }
}
