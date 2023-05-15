package com.esi2023.project.backupservice.dto;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BackupDto {
    @Id
    private Long id;
    private String request;
    private String response;
}

