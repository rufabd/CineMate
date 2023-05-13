package com.esiproject2023.metadataservice.repository;

import com.esiproject2023.metadataservice.model.MetadataEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MetadataRepository extends JpaRepository<MetadataEntry, Long> {
    Optional<MetadataEntry> findByRequest(String request);
}
