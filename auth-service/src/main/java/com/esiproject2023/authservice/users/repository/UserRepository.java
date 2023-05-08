package com.esiproject2023.authservice.users.repository;

import com.esiproject2023.authservice.users.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    List<User> findByEmailPreferences(String emailPreferences);
}
