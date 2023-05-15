package com.esiproject2023.metadatabackup.controller;

import com.esiproject2023.metadatabackup.model.Metadata;
import com.esiproject2023.metadatabackup.service.MetadataService;
import com.google.gson.Gson;
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
    public MetadataController(MetadataService dataService) {
        this.dataService = dataService;
    }

    //    @Scheduled(cron = "0 * * * * *")
    @GetMapping("/search")
    public ResponseEntity<Metadata[]> getAllMetadata() {
        try {
            String response = dataService.getResponse();
            String path = "/search";
            Metadata[] allMetadata = dataService.processResponse(response, path);
//            if (allMetadata.length != 0) {
//                metadataDBService.createRequest("/search", gson.toJson(allMetadata));
//            } else {
//                allMetadata = metadataDBService.getMetadataFromBackup("/search");
//            }
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
            String path = "/searchByParams/" + params;
            Metadata[] allMetadata = dataService.processResponse(response, path);
//            if (allMetadata.length != 0) {
//                metadataDBService.createRequest("/searchByParams/" + params, gson.toJson(allMetadata));
//            } else {
//                allMetadata = metadataDBService.getMetadataFromBackup("/searchByParams/" + params);
//            }
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
            String path = "/searchByIDs/" + ids;
            Metadata[] allMetadata = dataService.processResponse(response, path);
//            if (allMetadata.length != 0) {
//                metadataDBService.createRequest("/searchByIDs/" + ids, gson.toJson(allMetadata));
//            } else {
//                allMetadata = metadataDBService.getMetadataFromBackup("/searchByIDs/" + ids);
//            }
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
            String path = "/searchByID/" + id;
            Metadata allMetadata = dataService.processResponse(metadata, path)[0];
//            if (allMetadata.title() != null) {
//                metadataDBService.createRequest("/searchByIDs/" + id, gson.toJson(allMetadata));
//            } else {
//                Metadata[] metadata1 = metadataDBService.getMetadataFromBackup("/searchByIDs/" + id);
//                if (metadata1.length != 0) {
//                    allMetadata = metadata1[0];
//                }
//            }
            return ResponseEntity.ok(gson.toJson(allMetadata + reviews));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    @GetMapping("/status")
    public ResponseEntity<String> getMicroserviceStatus() {
            return ResponseEntity.ok("OK");
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
