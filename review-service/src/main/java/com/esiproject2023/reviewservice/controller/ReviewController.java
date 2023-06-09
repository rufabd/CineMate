package com.esiproject2023.reviewservice.controller;

import com.esiproject2023.reviewservice.dto.ReviewDto;
import com.esiproject2023.reviewservice.model.Review;
import com.esiproject2023.reviewservice.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public String addReview(@RequestBody ReviewDto reviewDto) {
        return reviewService.createReview(reviewDto);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/all")
    public List<ReviewDto> getReviews() {
        return reviewService.getAllReviews();
    }

    @GetMapping("/view/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<ReviewDto> getReview(@PathVariable Long id) {
        return reviewService.getReview(id);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String deleteReview(@PathVariable Long id) {
        return reviewService.deleteReview(id);
    }

    @GetMapping("/content/{contentId}")
    @ResponseStatus(HttpStatus.OK)
    public List<Review> getUserWatchlist(@PathVariable String contentId) {
        return reviewService.getReviewsForSpecificContent(contentId);
    }
    @PostMapping("/get/rating")
    public String updateUserProfile(@RequestBody String request) {
        return reviewService.averageRating(request);
    };
}
