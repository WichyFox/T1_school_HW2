package com.example.HW_2.client_processing.repository;

import com.example.HW_2.client_processing.entity.BlacklistRegistry;
import com.example.HW_2.client_processing.enums.DocumentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BlacklistRegistryRepository extends JpaRepository<BlacklistRegistry, Long> {
    Optional<BlacklistRegistry> findByDocumentTypeAndDocumentId(DocumentType documentType, String documentId);
}
