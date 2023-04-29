package com.esiproject2023.metadataservice.controller;

import com.esiproject2023.metadataservice.model.Metadata;
import com.esiproject2023.metadataservice.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class DataController {
    private final DataService dataService;

    @Autowired
    public DataController(DataService dataService) {
        this.dataService = dataService;
    }

    @GetMapping("/searchBy/{params}")
    public ResponseEntity<Metadata[]> getRequiredMetadata(@PathVariable String params) {
        try {
            String response = dataService.getResponseWithParams(params);
            Metadata[] allMetadata = dataService.processResponse(response);
            return ResponseEntity.ok(allMetadata);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    @GetMapping("/search")
    public ResponseEntity<Metadata[]> getAllMetadata() {
        try {
            String response = dataService.getResponse();
            Metadata[] allMetadata = dataService.processResponse(response);
            return ResponseEntity.ok(allMetadata);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

}
