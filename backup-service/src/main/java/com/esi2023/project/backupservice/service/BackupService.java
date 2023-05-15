package com.esi2023.project.backupservice.service;


import com.esi2023.project.backupservice.model.Metadata;
import com.esi2023.project.backupservice.model.MetadataEntry;
import com.esi2023.project.backupservice.repository.BackupRepository;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;

@Slf4j
@Service
public class BackupService {

    @Autowired
    private BackupRepository backupRepository;

    private void addRequest(MetadataEntry metadataEntry) {
        Optional<MetadataEntry> row =  backupRepository.findByRequest(metadataEntry.getRequest());
        if(row.isEmpty()) {
            backupRepository.save(metadataEntry);
            log.info("Success");
        }
        else {
            metadataEntry.setId(row.get().getId());
            backupRepository.save(metadataEntry);
            log.info("Updated");
        }
    }

    @KafkaListener(topics = "backupRequest")
    public void createRequest(HashMap<String,String> hashmap) {

        String path = hashmap.get("path");
        String response = hashmap.get("response");

        MetadataEntry metadataEntry = MetadataEntry.builder()
                .response(response)
                .request(path)
                .build();
        addRequest(metadataEntry);
    }

    @KafkaListener(topics = "getBackup")
    public String getMetadataFromBackup(String request) {
        Optional<MetadataEntry> row = backupRepository.findByRequest(request);
        Gson gson = new Gson();
        if (row.isPresent()) {
            return row.get().getResponse();
        } else {
            return gson.toJson(new String[0]);
        }
    }
}
