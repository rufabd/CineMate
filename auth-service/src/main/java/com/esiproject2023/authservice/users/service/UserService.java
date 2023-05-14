package com.esiproject2023.authservice.users.service;

import com.esiproject2023.authservice.users.model.User;
import com.esiproject2023.authservice.users.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void addUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        log.info("New user has been successfully created with id {}", user.getId());
//        return user;
    }

    public Optional<User> getUserInfoForUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<User> getUserForPreference(String emailPreferences) {
        return userRepository.findByEmailPreferences(emailPreferences);
    }

//    public Optional<User> getUserForEmail(String email) {
//        userRepository.
//    }

    public void updateUserLastEmailSent(String email) throws ParseException {
        Optional<User> user = userRepository.findByEmail(email);
        if(user.isPresent()) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date now = sdf.parse(LocalDate.now().toString());
            user.get().setLastEmailSent(now.toString());
            userRepository.save(user.get());
        }
    }

    public String updateUserProfileInfo(User user) {
        Optional<User> requiredUser = userRepository.findByUsername(user.getUsername());
        if(requiredUser.isPresent()) {
            requiredUser.get().setFullName(user.getFullName());
            requiredUser.get().setEmail(user.getEmail());
            requiredUser.get().setPassword(passwordEncoder.encode(user.getPassword()));
            requiredUser.get().setFavGenre(user.getFavGenre());
            requiredUser.get().setMinRating(user.getMinRating());
            requiredUser.get().setDob(user.getDob());
            requiredUser.get().setEmailPreferences(user.getEmailPreferences());
            User savedUser = userRepository.save(requiredUser.get());
            return "Success";
        } else {
            return "Fail";
        }
    }
}
