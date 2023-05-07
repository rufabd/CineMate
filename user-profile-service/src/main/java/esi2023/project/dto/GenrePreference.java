package esi2023.project.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum GenrePreference {

    ACTION("Action"),
    ADULT("Adult"),
    ADVENTURE("Adventure"),
    ANIMATION("Animation"),
    BIOGRAPHY("Biography"),
    COMEDY("Comedy"),
    CRIME("Crime"),
    DOCUMENTARY("Documentary"),
    DRAMA("Drama"),
    FAMILY("Family"),
    FANTASY("Fantasy"),
    FILM_NOIR("Film-Noir"),
    GAME_SHOW("Game-Show"),
    HISTORY("History"),
    HORROR("Horror"),
    MUSIC("Music"),
    MUSICAL("Musical"),
    MYSTERY("Mystery"),
    NEWS("News"),
    REALITY_TV("Reality-TV"),
    ROMANCE("Romance"),
    SCI_FI("Sci-Fi"),
    SHORT("Short"),
    SPORT("Sport"),
    TALK_SHOW("Talk-Show"),
    THRILLER("Thriller"),
    WAR("War"),
    WESTERN("Western");

    private final String genreName;

    public GenrePreference fromString(String preferenceString) {
        for (GenrePreference preference : GenrePreference.values()) {
            if (preference.getGenreName().equalsIgnoreCase(preferenceString)) {
                return preference;
            }
        }
        throw new IllegalArgumentException("Invalid genre preference: " + preferenceString);
    }

}
