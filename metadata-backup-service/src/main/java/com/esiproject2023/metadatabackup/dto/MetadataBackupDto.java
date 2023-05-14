package com.esiproject2023.metadatabackup.dto;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MetadataBackupDto {
    @Id
    private Long id;
    private String request;
    private String response;
}
