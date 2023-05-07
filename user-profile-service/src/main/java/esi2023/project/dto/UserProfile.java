package esi2023.project.dto;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


public record UserProfile(String username, String email, String fullName, String frequencyPreference, List<String> genrePreferences) {
//
//    private String username;
//
//    private String email;
//
//    private String fullName;
//
//    @Enumerated(EnumType.STRING)
//    private FrequencyPreference frequencyPreference;
//
//    @ElementCollection
//    @Enumerated(EnumType.STRING)
//    private List<GenrePreference> genrePreferences;
}
