package esi2023.project.dto;

import java.time.LocalDate;
import java.util.List;


public record UserProfile(String username, String email, String fullName, LocalDate dob, String frequencyPreference, List<String> genrePreferences) { }
