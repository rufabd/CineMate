package com.esiproject2023.metadatabackup.service;

import com.esiproject2023.metadatabackup.model.Metadata;
import com.esiproject2023.metadatabackup.model.MetadataEntry;
import com.esiproject2023.metadatabackup.repository.MetadataRepository;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class MetadataDBService {
    @Autowired
    private MetadataRepository metadataRepository;

    private void addRequest(MetadataEntry metadataEntry) {
        Optional<MetadataEntry> row =  metadataRepository.findByRequest(metadataEntry.getRequest());
        if(row.isEmpty()) {
            metadataRepository.save(metadataEntry);
            log.info("Success");
        }
        else {
            metadataEntry.setId(row.get().getId());
            metadataRepository.save(metadataEntry);
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
    public Metadata[] getMetadataFromBackup(String request) {
        Optional<MetadataEntry> row = metadataRepository.findByRequest(request);
        Gson gson = new Gson();
        if (row.isPresent()) {
            return gson.fromJson(row.get().getResponse(), Metadata[].class);
        } else {
            return new Metadata[0];
        }
    }
}
