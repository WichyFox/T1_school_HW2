package com.example.HW_2.client_processing.service;

import com.example.HW_2.client_processing.entity.BlacklistRegistry;
import com.example.HW_2.client_processing.enums.DocumentType;
import com.example.HW_2.client_processing.repository.BlacklistRegistryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class BlacklistService {

    private final BlacklistRegistryRepository blacklistRepository;

    public boolean isBlacklisted(DocumentType type, String documentId) {
        return blacklistRepository.findByDocumentTypeAndDocumentId(type, documentId)
                .filter(record -> record.getBlacklistExpirationDate() == null
                        || record.getBlacklistExpirationDate().isAfter(LocalDate.now()))
                .isPresent();
    }

    public BlacklistRegistry addToBlacklist(DocumentType type, String documentId, String reason, LocalDate expireAt) {
        BlacklistRegistry record = BlacklistRegistry.builder()
                .documentType(type)
                .documentId(documentId)
                .blacklistedAt(LocalDate.now())
                .reason(reason)
                .blacklistExpirationDate(expireAt)
                .build();
        return blacklistRepository.save(record);
    }
}
