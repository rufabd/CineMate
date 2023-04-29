package com.esiproject2023.reviewservice.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="reviews")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer user_id;
    private String content_id;
    private String body;
    private Double score;
}
