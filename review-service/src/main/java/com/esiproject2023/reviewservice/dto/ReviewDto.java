package com.esiproject2023.reviewservice.dto;

//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDto {
    private Long id;
    private Integer user_id;
    private String content_id;
    private String body;
    private Double score;
}
