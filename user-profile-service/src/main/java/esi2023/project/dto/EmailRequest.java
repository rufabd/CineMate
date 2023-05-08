package esi2023.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public record EmailRequest(String userEmail, String title, String subject, String content) { }
