package esi2023.project.service;

import esi2023.project.dto.Content;
import esi2023.project.dto.EmailRequest;
import esi2023.project.dto.UserProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    @Autowired
    private final WebClient.Builder webClient;
    private final KafkaTemplate<String, EmailRequest> kafkaTemplate;

    /**
     * runs at 9:00 AM daily to check which users should be emailed
     */
    @Scheduled(cron = "0 0 9 * * *")
//    @Scheduled(fixedDelay = 1000)
    public void sendEmails() {
        UserProfile[] dailyUsers = webClient.build().get().uri("http://auth-service/auth/{emailPreferences}", "daily").retrieve().bodyToMono(UserProfile[].class).block();
//        var dailyUsers = UserProfileFactory.getRandomUserProfiles(32, "daily");
        if(dailyUsers != null) sendEmailToUsers(dailyUsers);


        if (LocalDate.now().getDayOfWeek().equals(DayOfWeek.WEDNESDAY) || LocalDate.now().getDayOfWeek().equals(DayOfWeek.FRIDAY)) {
            UserProfile[] twiceWeeklyUsers = webClient.build().get().uri("http://auth-service/auth/{emailPreferences}", "twiceWeeklyUsers").retrieve().bodyToMono(UserProfile[].class).block();
            UserProfile[] thriceWeeklyUsers = webClient.build().get().uri("http://auth-service/auth/{emailPreferences}", "thriceWeeklyUsers").retrieve().bodyToMono(UserProfile[].class).block();
            //             var thriceWeeklyUsers = webClient.build().get().uri("USER_MS_URL", "thricePerWeek").retrieve().bodyToMono(UserProfile[].class).block();
//             var twiceWeeklyUsers = UserProfileFactory.getRandomUserProfiles(23, "twicePerWeek");
//             var thriceWeeklyUsers = UserProfileFactory.getRandomUserProfiles(10, "thricePerWeek");
            if(twiceWeeklyUsers != null) {
                sendEmailToUsers(twiceWeeklyUsers);
            }
            if(thriceWeeklyUsers != null) {
                sendEmailToUsers(thriceWeeklyUsers);
            }
        }

        if (LocalDate.now().getDayOfWeek().equals(DayOfWeek.MONDAY)) {
//             var thriceWeeklyUsers = webClient.build().get().uri("USER_MS_URL", "thricePerWeek").retrieve().bodyToMono(UserProfile[].class).block();
            UserProfile[] thriceWeeklyUsers = webClient.build().get().uri("http://auth-service/auth/{emailPreferences}", "thriceWeeklyUsers").retrieve().bodyToMono(UserProfile[].class).block();
//             var thriceWeeklyUsers = UserProfileFactory.getRandomUserProfiles(23, "thricePerWeek");
//             var weeklyUsers = webClient.build().get().uri("USER_MS_URL", "weekly").retrieve().bodyToMono(UserProfile[].class).block();
//             var weeklyUsers = UserProfileFactory.getRandomUserProfiles(53, "weekly");
            UserProfile[] weeklyUsers = webClient.build().get().uri("http://auth-service/auth/{emailPreferences}", "weeklyUsers").retrieve().bodyToMono(UserProfile[].class).block();
            if(thriceWeeklyUsers != null) {
                sendEmailToUsers(thriceWeeklyUsers);
            }
            if(weeklyUsers != null) {
                sendEmailToUsers(weeklyUsers);
            }
        }

        if (LocalDate.now().getDayOfMonth() == 1) {
            UserProfile[] monthlyUsers = webClient.build().get().uri("http://auth-service/auth/{emailPreferences}", "monthlyUsers").retrieve().bodyToMono(UserProfile[].class).block();
//             var monthlyUsers = webClient.build().get().uri("USER_MS_URL", "monthly").retrieve().bodyToMono(UserProfile[].class).block();
//             var monthlyUsers = UserProfileFactory.getRandomUserProfiles(8, "monthly");
            if(monthlyUsers != null) {
                sendEmailToUsers(monthlyUsers);
            }
        }
    }

    /**
     * sends email to a given set of users
     *
     * @param users set of users who should be emailed
     */
    private void sendEmailToUsers(UserProfile[] users) {
        Random random = new Random();
        for (var user : users) {
            var genre = user.favGenre();
            int randomRating = random.nextInt(9) + 1;
            var params = generateRequestParams(user, genre, randomRating);
            var content = webClient.build().get().uri("http://discovery-service/discovery/{params}", params).retrieve().bodyToMono(Content[].class).block();
            if(content != null) {
                var email = createEmail(user, content[0]);
                kafkaTemplate.send("emailTopic", email);
            }
        }
    }

    private String generateRequestParams(UserProfile user, String randomGenre, int randomRating) {
        int month = Integer.parseInt(user.dob().split("-")[1]);
        int day = Integer.parseInt(user.dob().split("-")[2]);
        int year = Integer.parseInt(user.dob().split("-")[0]);
        String monthStr = month < 10 ? "0" + month : Integer.toString(month);
        String dayStr = day < 10 ? "0" + day : Integer.toString(day);
        String yearStr = Integer.toString(year);
        return monthStr + "-" + dayStr + "-" + yearStr + "," + randomGenre + "," + randomRating;
    }

    /**
     * creates email template
     *
     * @param user user to whom the email is addressed
     * @param content content which the email body is about
     * @return
     */
    private EmailRequest createEmail(UserProfile user, Content content) {

        return new EmailRequest(
                user.email(),
                "Thank you very much for being a loyal subscriber, " + user.username() + "!",
                content.title(),
                "\nBecause you have subscribed to " + content.genre() + "Here's your movie recommendation for this time: " + content + "\n\nWe wish you lots of fun while!\n\nSincerely,\nTeam CineMate!");
    }
}
