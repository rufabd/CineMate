package com.esiproject2023.metadataservice.controller;

import com.esiproject2023.metadataservice.dto.MetadataDto;
import com.esiproject2023.metadataservice.model.Metadata;
import com.esiproject2023.metadataservice.service.MetadataDBService;
import com.esiproject2023.metadataservice.service.MetadataService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import jakarta.ws.rs.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/metadata")
public class MetadataController {
    @Autowired
    private final MetadataService dataService;

    @Autowired
    private Gson gson;

    @Autowired
    private MetadataDBService metadataDBService;

    @Autowired
    public MetadataController(MetadataService dataService) {
        this.dataService = dataService;
    }

    @Scheduled(cron = "0 * * * *")
    @GetMapping("/search")
    public ResponseEntity<Metadata[]> getAllMetadata() {
        try {
            String response = dataService.getResponse();
            Metadata[] allMetadata = dataService.processResponse(response);
            if (allMetadata.length != 0) {
                metadataDBService.createRequest("/search", gson.toJson(allMetadata));
            } else {
                allMetadata = metadataDBService.getMetadataFromBackup("/search");
            }
            return ResponseEntity.ok(allMetadata);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/searchByParams/{params}")
    public ResponseEntity<Metadata[]> getRequiredMetadata(@PathVariable String params) {
        try {
            String response = dataService.getResponseWithParams(params);
            Metadata[] allMetadata = dataService.processResponse(response);
            if (allMetadata.length != 0) {
                metadataDBService.createRequest("/searchByParams/" + params, gson.toJson(allMetadata));
            } else {
                allMetadata = metadataDBService.getMetadataFromBackup("/searchByParams/" + params);
            }
            return ResponseEntity.ok(allMetadata);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/searchByIDs/{ids}")
    public ResponseEntity<Metadata[]> getAllMetadataByIDs(@PathVariable String ids) {
        try {
            String response = dataService.getResponseWithIDs(ids);
            Metadata[] allMetadata = dataService.processResponse(response);
            if (allMetadata.length != 0) {
                metadataDBService.createRequest("/searchByIDs/" + ids, gson.toJson(allMetadata));
            } else {
                allMetadata = metadataDBService.getMetadataFromBackup("/searchByParams/" + ids);
            }
            return ResponseEntity.ok(allMetadata);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/searchByID/{id}")
    public ResponseEntity<String> getMetadataAndRatingByID(@PathVariable String id) {
        try {
            Gson gson = new Gson();
            String metadata = dataService.getResponseWithIDs(id);
            String reviews = dataService.getReviewByID(id);
            Metadata allMetadata = dataService.processResponse(metadata)[0];
            if (allMetadata.title() != null) {
                metadataDBService.createRequest("/searchByIDs/" + id, gson.toJson(allMetadata));
            } else {
                Metadata[] metadata1 = metadataDBService.getMetadataFromBackup("/searchByParams/" + id);
                if (metadata1.length != 0) {
                    allMetadata = metadata1[0];
                }
            }
            return ResponseEntity.ok(gson.toJson(allMetadata + reviews));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

}
