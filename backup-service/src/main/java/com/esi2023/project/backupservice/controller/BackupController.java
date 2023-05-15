package com.esi2023.project.backupservice.controller;


import com.esi2023.project.backupservice.model.Metadata;
import com.esi2023.project.backupservice.service.BackupService;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@Slf4j
@RestController
@RequestMapping("/backup")
public class BackupController {

    @Autowired
    private Gson gson;

    @Autowired
    private BackupService backupService;


    @GetMapping("/save/{metadata}/{request}")
    public ResponseEntity<String> saveJSON(@PathVariable Metadata[] metadata, @PathVariable String request) {
        try {
            HashMap<String, String> obj = new HashMap<>();
            obj.put("path",request);
            obj.put("response",gson.toJson(metadata));
            backupService.createRequest(obj);
            return ResponseEntity.ok("OK");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/get/{request}")
    public ResponseEntity<String> getAllMetadata(@PathVariable String request) {
        try {
            String string = backupService.getMetadataFromBackup(request);
            return ResponseEntity.ok(string);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
}
