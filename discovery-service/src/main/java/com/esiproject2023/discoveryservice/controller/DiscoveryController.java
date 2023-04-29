package com.esiproject2023.discoveryservice.controller;


import com.esiproject2023.discoveryservice.model.Content;
import com.esiproject2023.discoveryservice.service.DiscoveryService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class DiscoveryController {
    private final DiscoveryService discoveryService;

    @Autowired
    public DiscoveryController(DiscoveryService discoveryService) {
        this.discoveryService = discoveryService;
    }

    @GetMapping("/discovery/search/{params}") //search section
    public ResponseEntity<Content[]> getDiscoveryDataBySearch(@PathVariable String params) {
        //searchedTitle
        /// add "/search/akas/TITLENAME"
        // only then add additional params by calling method createConfig()
        return ResponseEntity.ok(new Content[0]);
    }

    @GetMapping("/discovery/{params}") //FOR YOU
    public ResponseEntity<Content[]> getDiscoveryData(@PathVariable String params) {
        try {
            Content[] top10Content = discoveryService.processResponse(params);
            return ResponseEntity.ok(top10Content);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
