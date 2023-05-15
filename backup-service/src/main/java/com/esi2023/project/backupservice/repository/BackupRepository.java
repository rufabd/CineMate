package com.esi2023.project.backupservice.repository;

import com.esi2023.project.backupservice.model.MetadataEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BackupRepository extends JpaRepository<MetadataEntry, Long> {
        Optional<MetadataEntry> findByRequest(String request);
        }
