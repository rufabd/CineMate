package com.esiproject2023.metadataservice.controller;

import com.esiproject2023.metadataservice.model.Metadata;
import com.esiproject2023.metadataservice.service.MetadataService;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/metadata")
public class MetadataController {
    @Autowired
    private final MetadataService dataService;

    @Autowired
    public MetadataController(MetadataService dataService) {
        this.dataService = dataService;
    }

    //    @Scheduled(cron = "0 * * * * *")
    @GetMapping("/search")
    public ResponseEntity<Metadata[]> getAllMetadata() {
        try {
            String response = dataService.getResponse();
            String path = "search";
            Metadata[] allMetadata = dataService.processResponse(response, path);
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
            String path = "searchByParams-" + params;
            Metadata[] allMetadata = dataService.processResponse(response, path);
            return ResponseEntity.ok(allMetadata);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/searchByIDs/{params}")
    public ResponseEntity<Metadata[]> getAllMetadataByIDs(@PathVariable String params) {
        try {
            String response = dataService.getResponseWithIDs(params);
            String path = "searchByIDs-" + params;
            Metadata[] allMetadata = dataService.processResponse(response, path);
            return ResponseEntity.ok(allMetadata);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/searchByID/{params}")
    public ResponseEntity<String> getMetadataAndRatingByID(@PathVariable String params) {
        try {
            Gson gson = new Gson();
            String metadata = dataService.getResponseWithIDs(params);
            String reviews = dataService.getReviewByID(params);
            String path = "searchByID-" + params;
            Metadata allMetadata = dataService.processResponse(metadata, path)[0];
            return ResponseEntity.ok(gson.toJson(allMetadata + reviews));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/genres")
    public ResponseEntity<String[]> getGenres() {
        try {
            String response = dataService.getResponseWithParams("/utils/genres");
            String[] genres = dataService.processGenresResponse(response).toArray(new String[0]);
            return ResponseEntity.ok(genres);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

}
