package com.esiproject2023.metadataservice.service;

import com.esiproject2023.metadataservice.dto.MetadataDto;
import com.esiproject2023.metadataservice.model.Metadata;
import com.esiproject2023.metadataservice.model.MetadataEntry;
import com.esiproject2023.metadataservice.repository.MetadataRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Meta;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Optional;

@Service
public class MetadataDBService {
    @Autowired
    private MetadataRepository metadataRepository;

    public String addRequest(MetadataEntry metadataEntry) {
        Optional<MetadataEntry> row =  metadataRepository.findByRequest(metadataEntry.getRequest());
        if(row.isEmpty()) {
            metadataRepository.save(metadataEntry);
            return "Success";
        }
        else {
            metadataEntry.setId(row.get().getId());
            metadataRepository.save(metadataEntry);
            return "Updated";
        }
    }

    public String createRequest(String path, String response) {
        MetadataEntry metadataEntry = MetadataEntry.builder()
                .response(response)
                .request(path)
                .build();
        return addRequest(metadataEntry);
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


//    @Scheduled(cron = "0 * * * *")
    public void updateDB(){
        //TODO
    }

//    public Optional<>


}
