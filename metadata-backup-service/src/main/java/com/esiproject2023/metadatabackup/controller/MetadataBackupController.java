package com.esiproject2023.metadatabackup.controller;

import com.esiproject2023.metadatabackup.model.Metadata;
import com.esiproject2023.metadatabackup.service.MetadataDBService;
import com.google.gson.Gson;
import jakarta.ws.rs.Path;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/backup")
public class MetadataBackupController {

    @Autowired
    private Gson gson;

    @Autowired
    private MetadataDBService metadataDBService;


    @GetMapping("/save/{metadata}/{request}")
    public ResponseEntity<String> saveJSON(@PathVariable Metadata[] metadata, @PathVariable String request) {
        try {
            HashMap<String, String> obj = new HashMap<>();
            obj.put("path",request);
            obj.put("response",gson.toJson(metadata));
            metadataDBService.createRequest(obj);
            return ResponseEntity.ok("OK");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/get/{request}")
    public ResponseEntity<Metadata[]> getAllMetadata(@PathVariable String request) {
        try {
            Metadata[] allMetadata = metadataDBService.getMetadataFromBackup(request);
            return ResponseEntity.ok(allMetadata);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

}
