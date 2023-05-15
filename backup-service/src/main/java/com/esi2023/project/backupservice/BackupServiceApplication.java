package com.esi2023.project.backupservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class BackupServiceApplication {

    public static void main(String[] args) {
        DatabaseInitializer.initialize("db_metadata");
        SpringApplication.run(BackupServiceApplication.class, args);
    }

}
