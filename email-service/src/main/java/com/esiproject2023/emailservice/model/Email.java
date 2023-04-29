package com.esiproject2023.emailservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Table(name="emails")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Email {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String user_email;
    private String title;
    private String subject;
    private String content;
    @Column(columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp timestamp;
}
