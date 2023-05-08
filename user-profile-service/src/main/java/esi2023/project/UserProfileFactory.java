package esi2023.project;

import esi2023.project.dto.UserProfile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class UserProfileFactory {

    private static final String[] FULL_NAMES = {"Emma Smith", "Liam Johnson", "Olivia Brown", "Noah Garcia", "Ava Miller",
            "William Davis", "Sophia Rodriguez", "James Martinez", "Isabella Hernandez", "Benjamin Lopez",
            "Mia Gonzalez", "Jacob Perez", "Charlotte Wilson", "Michael King", "Amelia Wright", "Ethan Scott",
            "Emily Green", "Alexander Baker", "Abigail Adams", "Daniel Nelson", "Madison Carter", "Matthew Mitchell",
            "Elizabeth Perez", "David Roberts", "Ella Turner", "Joseph Phillips", "Sofia Campbell", "Jackson Parker",
            "Avery Evans", "Sebastian Edwards", "Harper Collins", "Samuel Stewart", "Evelyn Sanchez", "Gabriel Morris",
            "Victoria Rogers", "Caleb Reed", "Grace Cook", "Luke Bailey", "Chloe Rivera", "Owen Cooper",
            "Isabelle Bailey", "Ryan Howard", "Samantha Kim", "Nathan Cox", "Aaliyah Ward", "Isaac Reyes",
            "Natalie Flores", "Elijah Peterson", "Lily Mitchell", "Hannah Price", "Grayson Powell", "Aria Turner"};

    private static final String[] GENRE_PREFERENCES = {"Action", "Adult", "Adventure", "Animation", "Biography",
            "Comedy", "Crime", "Documentary", "Drama", "Family", "Fantasy", "Film-Noir", "Game-Show", "History",
            "Horror", "Music", "Musical", "Mystery", "News", "Reality-TV", "Romance", "Sci-Fi", "Short", "Sport",
            "Talk-Show", "Thriller", "War", "Western"};

    private static final String[] EMAIL_PROVIDERS = {"gmail.com", "yahoo.com", "hotmail.com", "outlook.com"};

    private static final String[] FREQUENCY_PREFERENCES = {"daily", "weekly", "twicePerWeek", "thricePerWeek", "monthly"};

    private static final Random random = new Random();

    public static UserProfile[] getRandomUserProfiles(int count, String preference) {
        UserProfile[] userProfiles = new UserProfile[count];
        for (int i = 0; i < count; i++) {
            String fullName = FULL_NAMES[random.nextInt(FULL_NAMES.length)];
            String[] nameParts = fullName.split(" ");
            String firstName = nameParts[0];
            String lastName = nameParts[1];
            String username = firstName.toLowerCase() + "_" + lastName.toLowerCase() + "_" + random.nextInt(100);
            String email = firstName.toLowerCase() + "." + lastName.toLowerCase() + "@" + EMAIL_PROVIDERS[random.nextInt(EMAIL_PROVIDERS.length)];
            String frequencyPreference = FREQUENCY_PREFERENCES[random.nextInt(FREQUENCY_PREFERENCES.length)];
            List<String> genrePreferences = new ArrayList<>();
            int numGenres = random.nextInt(GENRE_PREFERENCES.length);
            for (int j = 0; j < numGenres; j++) {
                genrePreferences.add(GENRE_PREFERENCES[random.nextInt(GENRE_PREFERENCES.length)]);
            }
            LocalDate dob = LocalDate.of(
                    random.nextInt(66) + 1950, // year (1950-2015)
                    random.nextInt(12) + 1, // month (1-12)
                    random.nextInt(28) + 1 // day (1-28)
            );
            userProfiles[i] = new UserProfile(username, email, fullName, dob, preference, genrePreferences);
        }
        return userProfiles;
    }
}