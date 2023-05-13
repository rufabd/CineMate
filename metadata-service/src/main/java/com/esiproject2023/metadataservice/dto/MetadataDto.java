package com.esiproject2023.metadataservice.dto;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MetadataDto {
    @Id
    private Long id;
    private String request;
    private String response;
}
