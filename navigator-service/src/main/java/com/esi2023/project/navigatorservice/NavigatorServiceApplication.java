package com.esi2023.project.navigatorservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
@EnableDiscoveryClient
public class NavigatorServiceApplication {
	@LoadBalanced
	@Bean
	public WebClient.Builder webClient() {
		return WebClient.builder();
	}

	public static void main(String[] args) {
		SpringApplication.run(NavigatorServiceApplication.class, args);
	}

}
