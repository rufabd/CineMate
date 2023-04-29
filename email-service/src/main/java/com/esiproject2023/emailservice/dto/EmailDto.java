package com.esiproject2023.emailservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailDto {
    private Long id;
    private String user_email;
    private String title;
    private String subject;
    private String content;
    private Timestamp timestamp;
}
