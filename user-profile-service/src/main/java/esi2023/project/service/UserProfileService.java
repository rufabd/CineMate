package esi2023.project.service;

import esi2023.project.dto.Content;
import esi2023.project.dto.DiscoveryRequest;
import esi2023.project.dto.EmailRequest;
import esi2023.project.dto.UserProfile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
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

    private final KafkaTemplate<String, EmailRequest> kafkaTemplate;

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
                    return dateChecker(user.lastEmailSent(), 7);
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

    @Scheduled(cron = "0 0 8 * * *")
    public void sendEmailsToUsers() {
        List<List<UserProfile>> wholeList = new ArrayList<>();
        wholeList.add(getUsersDaily());
        wholeList.add(getUsersOncePerWeek());
        wholeList.add(getUsersoncePerMonth());
        for (List<UserProfile> userProfiles : wholeList) {
            for (UserProfile userProfile : userProfiles) {
                DiscoveryRequest discoveryRequest = new DiscoveryRequest(userProfile.dob(), userProfile.favGenre(), userProfile.minRating());
                Content[] content = webClient.build().get().uri("http://discovery-service/discovery/{params}", discoveryRequest.getDob() + "," + discoveryRequest
                        .getGenre() + "," + discoveryRequest.getRating()).retrieve().bodyToMono(Content[].class).block();
                if (content != null) {
                    String emailType;
                    if (userProfile.emailPreferences().equals("daily")) emailType = "discoveryDaily";
                    else emailType = "discovery";
                    EmailRequest emailRequest = new EmailRequest(
                            userProfile.email(),
                            "Hello, dear user and subscriber!\n", "Your daily content recommendation",
                            "\nYour daily content recommendation is here! For your taste, we recommend you to check " + content[0].title() + ".\n" +
                                    "\n" + content[0].description() + "\n\n" +
                                    "The cast includes popular actors and actresses like " + content[0].cast() + "\n\n" +
                                    content[0].poster() +
                                    "\n\nGenre: " + content[0].genre() + "\n\n" +
                                    "Rating: " + content[0].rating() + "\n" +
                                    "Director: " + content[0].director() + "\n" +
                                    "Release date: " + content[0].release_date() + "\n\n" +
                                    "Thank you very much for subscribing to our newsletter. We always try to develop our platform, so thank you very much for supporting us!" + "\n\n" +
                                    "We wish you to have a nice day!" +
                                    "\n\n" + "Sincerely,\n" + "Rufat Abdullayev | Team CineMate", emailType
                    );
                    kafkaTemplate.send("discoveryTopic", emailRequest);
                }
            }
        }
    }

    public boolean dateChecker(String lastEmailSent, int daysShouldBePassed) throws ParseException {
        if(!lastEmailSent.equals("")) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date now = sdf.parse(LocalDate.now().toString());
            Date userEmailLastSent = sdf.parse(lastEmailSent);
            long diffInMillis = Math.abs(now.getTime() - userEmailLastSent.getTime());
            long diff = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);
            return diff == daysShouldBePassed;
        }
        else return true;
    }
}