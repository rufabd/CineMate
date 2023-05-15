package com.esiproject2023.emailservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
@EnableDiscoveryClient
public class EmailServiceApplication {

	@LoadBalanced
	@Bean
	public WebClient.Builder webClient() {
		return WebClient.builder();
	}
	public static void main(String[] args) {
		DatabaseInitializer.initialize("db_emails");
		SpringApplication.run(EmailServiceApplication.class, args);
	}
}
