package esi2023.project.dto;

import java.util.List;


public record UserProfile(String username, String email, String fullName, String frequencyPreference, List<String> genrePreferences) { }
