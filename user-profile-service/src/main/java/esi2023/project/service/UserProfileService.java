package esi2023.project.service;

import esi2023.project.dto.UserProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserProfileService {
    @Autowired
    private WebClient.Builder webClient;

//    Get all users with types of emailPreferences: oncePerWeek, oncePerMonth, daily, twicePerWeek
    public List<UserProfile> getUsersDaily() {
        UserProfile[] response = webClient.build().post().uri("http://auth-service/auth/emailPreferences").body(Mono.just("daily"), String.class).retrieve().bodyToMono(UserProfile[].class).block();
        if(response != null) return List.of(response);
        else return List.of();
    }

    public List<UserProfile> getUserstwicePerWeek() {
        UserProfile[] response = webClient.build().post().uri("http://auth-service/auth/emailPreferences").body(Mono.just("twicePerWeek"), String.class).retrieve().bodyToMono(UserProfile[].class).block();
        if(response != null) return List.of(response);
        else return List.of();
    }

    public List<UserProfile> getUsersOncePerWeek() {
        UserProfile[] response = webClient.build().post().uri("http://auth-service/auth/emailPreferences").body(Mono.just("oncePerWeek"), String.class).retrieve().bodyToMono(UserProfile[].class).block();
        if(response != null) return List.of(response);
        else return List.of();
    }

    public List<UserProfile> getUsersoncePerMonth() {
        UserProfile[] response = webClient.build().post().uri("http://auth-service/auth/emailPreferences").body(Mono.just("oncePerMonth"), String.class).retrieve().bodyToMono(UserProfile[].class).block();
        if(response != null) return List.of(response);
        else return List.of();
    }



}