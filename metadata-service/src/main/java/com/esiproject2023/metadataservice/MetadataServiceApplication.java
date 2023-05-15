package com.esiproject2023.metadataservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableDiscoveryClient
@EnableScheduling
public class MetadataServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MetadataServiceApplication.class, args);
	}

}
