package com.esi2023.project.navigatorservice.controller;


import com.esi2023.project.navigatorservice.service.NavigatorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/navigator")
public class NavigatorController {
    @Autowired
    private NavigatorService navigatorService;


    @GetMapping("/search")
    public ResponseEntity<String> processSearch() {
        try {
            String response = navigatorService.getRequest("search");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/genres")
    public ResponseEntity<String> processGenres() {
        try {
            String response = navigatorService.getRequest("genres");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/searchByID/{params}")
    public ResponseEntity<String> getMetadataAndRatingByID(@PathVariable String params) {
        try {
            String response = navigatorService.searchByParams("searchByID", params);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
    @GetMapping("/searchByIDs/{params}")
    public ResponseEntity<String> getAllMetadataByIDs(@PathVariable String params) {
        try {
            String response = navigatorService.searchByParams("searchByIDs", params);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
}
