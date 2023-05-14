package esi2023.project.service;

import esi2023.project.dto.UserProfile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserProfileService {
    @Autowired
    private WebClient.Builder webClient;

//    Get all users with types of emailPreferences: oncePerWeek, oncePerMonth, daily, twicePerWeek
    public List<UserProfile> getUsersDaily() {
        UserProfile[] response = webClient.build().post().uri("http://auth-service/auth/emailPreferences").body(Mono.just("daily"), String.class).retrieve().bodyToMono(UserProfile[].class).block();
        if(response != null) return List.of(response);
        else return List.of();
    }

    public List<UserProfile> getUsersOncePerWeek() {
        UserProfile[] response = webClient.build().post().uri("http://auth-service/auth/emailPreferences").body(Mono.just("oncePerWeek"), String.class).retrieve().bodyToMono(UserProfile[].class).block();
        if(response != null) {
            Stream<UserProfile> filteredList = Arrays.stream(response).filter(user -> {
                try {
                    return user.lastEmailSent()==null ||
                            dateChecker(user.lastEmailSent(), 7);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            });
            return filteredList.collect(Collectors.toList());
        }
        else return List.of();
    }

    public List<UserProfile> getUsersoncePerMonth() {
        UserProfile[] response = webClient.build().post().uri("http://auth-service/auth/emailPreferences").body(Mono.just("oncePerMonth"), String.class).retrieve().bodyToMono(UserProfile[].class).block();
        if(response != null) {
            Stream<UserProfile> filteredList = Arrays.stream(response).filter(user -> {
                try {
                    return user.lastEmailSent()==null ||
                            dateChecker(user.lastEmailSent(), 30);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            });
            return filteredList.collect(Collectors.toList());
        }
        else return List.of();
    }

    public boolean dateChecker(String lastEmailSent, int daysShouldBePassed) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date now = sdf.parse(LocalDate.now().toString());
        Date userEmailLastSent = sdf.parse(lastEmailSent);
        long diffInMillis = Math.abs(now.getTime() - userEmailLastSent.getTime());
        long diff = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);
        return diff == daysShouldBePassed;
    }
}