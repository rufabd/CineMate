package com.esiproject2023.authservice.users.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "c_user")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "fullName", nullable = false)
    private String fullName;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "role", nullable = false)
    private String role;

    @Column(name = "favGenre", nullable = false)
    private String favGenre;

    @Column(name = "dob", nullable = false)
    private String dob;

    @Column(name = "minRating", nullable = false)
    private String minRating;

    @Column(name = "emailPreferences", nullable = false)
    private String emailPreferences;

    @Column(name = "lastEmailSent")
    private String lastEmailSent;
}
