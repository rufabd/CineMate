package esi2023.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;



public record UserProfile(String username, String email, String fullName, String dob, String emailPreferences, String favGenre) { }
