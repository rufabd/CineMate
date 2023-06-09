package com.esiproject2023.authservice.users.controller;


import com.esiproject2023.authservice.jwt.JwtService;
import com.esiproject2023.authservice.users.dto.UserDto;
import com.esiproject2023.authservice.users.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import com.esiproject2023.authservice.users.model.User;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:8080", allowedHeaders = "*")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public String loginAndGetToken(@RequestBody UserDto userDto) {
        if(userDto.getUsername() == null || userDto.getPassword() == null) {
            throw new UsernameNotFoundException("Username or password is empty");
        }

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword()));

        if(authentication.isAuthenticated()) {
            log.info("jwtService.generateToken(authRequest.getName())  {} ", jwtService.generateToken(userDto.getUsername()).toString());
            return  jwtService.generateToken(userDto.getUsername());
        } else {
            throw new UsernameNotFoundException("The user can not be authenticated");
        }
    }

    @GetMapping("/authenticate")
    public Boolean authenticate(@RequestHeader("Authorization") String header) {
        String token = header.replace("Bearer ", "");
        log.info(" authenticate - token {} ", token);
        return  jwtService.validateToken(token);
    }

    @PostMapping("/register")
    public String signupUser(@RequestBody User user){
        userService.addUser(user);
        return jwtService.generateToken(user.getUsername());
    }

    @GetMapping("/admin")
    public String adminApi() {
        return "Only admins allowed!";
    }

    @GetMapping("/user")
    public String userApi() {
        return "Only users allowed!";
    }

    @GetMapping("/{username}")
    public Optional<User> getUserByUsername(@PathVariable String username) {
        return userService.getUserInfoForUsername(username);
    }

    @PostMapping("/emailPreferences")
    public List<User> getUsersForPreferences(@RequestBody String preferences) {
        return userService.getUserForPreference(preferences);
    }

    @PostMapping("/update/lastEmail")
    public void updateUserLastEmailSent(@RequestBody String email) throws ParseException {
        userService.updateUserLastEmailSent(email);
    }

    @PostMapping("/update/profile")
    public String updateUserProfile(@RequestBody User user) {
        return userService.updateUserProfileInfo(user);
    }
}
