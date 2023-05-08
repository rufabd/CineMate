package esi2023.project.controller;

import esi2023.project.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("userprofile")
public class UserProfileController {

    private final UserProfileService service;

    @PostMapping("/sendRecommendations")
    public void sendRecommendations() {
        service.sendEmails();
    }

}
