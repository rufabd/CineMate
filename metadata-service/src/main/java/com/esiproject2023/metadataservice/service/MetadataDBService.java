package com.esiproject2023.metadataservice.service;

import com.esiproject2023.metadataservice.model.Metadata;
import com.esiproject2023.metadataservice.model.MetadataEntry;
import com.esiproject2023.metadataservice.repository.MetadataRepository;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class MetadataDBService {
    @Autowired
    private MetadataRepository metadataRepository;

    public void addRequest(MetadataEntry metadataEntry) {
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

    public void createRequest(String path, String response) {
        MetadataEntry metadataEntry = MetadataEntry.builder()
                .response(response)
                .request(path)
                .build();
        addRequest(metadataEntry);
    }

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
