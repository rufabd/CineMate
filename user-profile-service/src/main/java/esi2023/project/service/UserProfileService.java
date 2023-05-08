package esi2023.project.service;

import esi2023.project.UserProfileFactory;
import esi2023.project.dto.Content;
import esi2023.project.dto.EmailRequest;
import esi2023.project.dto.UserProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final WebClient.Builder webClient;
    private final KafkaTemplate<String, EmailRequest> kafkaTemplate;

    /**
     * runs at 9:00 AM daily to check which users should be emailed
     */
    @Scheduled(cron = "0 0 9 * * *")
    public void sendEmails() {
//        var dailyUsers = webClient.build().get().uri("USER_MS_URL", "daily").retrieve().bodyToMono(UserProfile[].class).block();
        var dailyUsers = UserProfileFactory.getRandomUserProfiles(32, "daily");
        sendEmailToUsers(dailyUsers);

         if (LocalDate.now().getDayOfWeek().equals(DayOfWeek.WEDNESDAY) || LocalDate.now().getDayOfWeek().equals(DayOfWeek.FRIDAY)) {
//             var twiceWeeklyUsers = webClient.build().get().uri("USER_MS_URL", "twicePerWeek").retrieve().bodyToMono(UserProfile[].class).block();
//             var thriceWeeklyUsers = webClient.build().get().uri("USER_MS_URL", "thricePerWeek").retrieve().bodyToMono(UserProfile[].class).block();
             var twiceWeeklyUsers = UserProfileFactory.getRandomUserProfiles(23, "twicePerWeek");
             var thriceWeeklyUsers = UserProfileFactory.getRandomUserProfiles(10, "thricePerWeek");
             sendEmailToUsers(twiceWeeklyUsers);
             sendEmailToUsers(thriceWeeklyUsers);
         }

         if (LocalDate.now().getDayOfWeek().equals(DayOfWeek.MONDAY)) {
//             var thriceWeeklyUsers = webClient.build().get().uri("USER_MS_URL", "thricePerWeek").retrieve().bodyToMono(UserProfile[].class).block();
             var thriceWeeklyUsers = UserProfileFactory.getRandomUserProfiles(23, "thricePerWeek");
//             var weeklyUsers = webClient.build().get().uri("USER_MS_URL", "weekly").retrieve().bodyToMono(UserProfile[].class).block();
             var weeklyUsers = UserProfileFactory.getRandomUserProfiles(53, "weekly");
             sendEmailToUsers(thriceWeeklyUsers);
             sendEmailToUsers(weeklyUsers);
         }

         if (LocalDate.now().getDayOfMonth() == 1) {
//             var monthlyUsers = webClient.build().get().uri("USER_MS_URL", "monthly").retrieve().bodyToMono(UserProfile[].class).block();
             var monthlyUsers = UserProfileFactory.getRandomUserProfiles(8, "monthly");
             sendEmailToUsers(monthlyUsers);
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
            var genres = user.genrePreferences();
            Collections.shuffle(genres);
            var randomGenre = genres.get(0);
            int randomRating = random.nextInt(9) + 1;
            var params = generateRequestParams(user, randomGenre, randomRating);
            var content = webClient.build().get().uri("http://discovery-service/discovery/{params}", params).retrieve().bodyToMono(Content[].class).block();
            var email = createEmail(user, content[0]);
            kafkaTemplate.send("emailTopic", email);
        }
    }

    private String generateRequestParams(UserProfile user, String randomGenre, int randomRating) {
        int month = user.dob().getMonthValue();
        int day = user.dob().getDayOfMonth();
        int year = user.dob().getYear();
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
        EmailRequest email = new EmailRequest(
                user.email(),
                "Thank you very much for being a loyal subscriber, " + user.username() + "!",
                content.title(),
                "\nBecause you have subscribed to " + content.genre() + "Here's your movie recommendation for this time: " + content + "\n\nWe wish you lots of fun while!\n\nSincerely,\nTeam CineMate!");

        return email;
    }
}
