package com.esi2023.project.navigatorservice.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Slf4j
@Service
public class NavigatorService {

    @Autowired
    private WebClient.Builder webClient;

    public String getRequest(String path) {
        try {
            return webClient.build().get().uri("http://metadata-service/metadata/" + path).retrieve().bodyToMono(String.class).block();

        } catch (Exception e) {
            try {
                return webClient.build().get().uri("http://backup-service/backup/get/" + path).retrieve().bodyToMono(String.class).block();

            } catch (Exception x) {
                throw new RuntimeException("BOTH API AND BACKUP NOT WORKING");
            }
        }
    }

    public String searchByParams(String path, String params) {
        try {
            return webClient.build().get().uri("http://metadata-service/metadata/" + path + "/{params}", params).retrieve().bodyToMono(String.class).block();

        } catch (Exception e) {
            try {
                return webClient.build().get().uri("http://backup-service/backup/get/" + path + "-" + params).retrieve().bodyToMono(String.class).block();

            } catch (Exception x) {
                throw new RuntimeException("BOTH API AND BACKUP NOT WORKING");
            }
        }
    }
}
