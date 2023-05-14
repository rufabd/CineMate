package com.esiproject2023.reviewservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserResponse {
    String id;
    String fullName;
    String username;
    String email;
    String password;
    String role;
    String favGenre;
    String dob;
    String minRating;
    String emailPreferences;
    String lastEmailSent;
}
