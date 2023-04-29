package com.esiproject2023.reviewservice.service;

import com.esiproject2023.reviewservice.dto.ReviewDto;
import com.esiproject2023.reviewservice.model.Review;
import com.esiproject2023.reviewservice.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewService {
    @Autowired
    private final ReviewRepository reviewRepository;

//    public void createReview(ReviewDto reviewDto) {
//        Review review = Review.builder()
//                .user_id(reviewDto.getUser_id())
//                .content_id(reviewDto.getContent_id())
//                .body(reviewDto.getBody())
//                .score(reviewDto.getScore())
//                .build();
//        reviewRepository.save(review);
//        log.info("The review {} is added", review.getId());
//    }
    public ReviewDto createReview(ReviewDto reviewDto) {
        Review review = Review.builder()
                .user_id(reviewDto.getUser_id())
                .content_id(reviewDto.getContent_id())
                .body(reviewDto.getBody())
                .score(reviewDto.getScore())
                .build();
        Review result = reviewRepository.save(review);
        log.info("The review with id {} is added", review.getId());
        return mapToReviewDto(result);
    }

    public List<ReviewDto> getAllReviews() {
        List<Review> reviews = reviewRepository.findAll();
        return reviews.stream().map(this::mapToReviewDto).toList();
    }

    public ReviewDto mapToReviewDto(Review review) {
        return ReviewDto.builder()
                .id(review.getId())
                .user_id(review.getUser_id())
                .content_id(review.getContent_id())
                .body(review.getBody())
                .score(review.getScore())
                .build();
    }

    public Optional<ReviewDto> getReview(Long id) {
        Optional<Review> review = reviewRepository.findById(id);
        return review.map(this::mapToReviewDto);
    }

    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
        log.info("Product with id {} has been deleted", id);
    }

}
