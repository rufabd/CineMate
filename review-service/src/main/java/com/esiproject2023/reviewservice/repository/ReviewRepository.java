package com.esiproject2023.reviewservice.repository;

import com.esiproject2023.reviewservice.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
