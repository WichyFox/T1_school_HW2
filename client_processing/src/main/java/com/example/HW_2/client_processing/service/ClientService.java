package com.example.HW_2.client_processing.service;

import com.example.HW_2.client_processing.dto.ClientRegistrationRequest;
import com.example.HW_2.client_processing.entity.BlacklistRegistry;
import com.example.HW_2.client_processing.entity.Client;
import com.example.HW_2.client_processing.repository.BlacklistRegistryRepository;
import com.example.HW_2.client_processing.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final BlacklistRegistryRepository blacklistRegistryRepository;

    public boolean isBlacklisted(com.example.HW_2.client_processing.enums.DocumentType type, String documentId) {
        Optional<BlacklistRegistry> registry = blacklistRegistryRepository.findByDocumentTypeAndDocumentId(type, documentId);
        return registry.isPresent();
    }

    public Client createClient(ClientRegistrationRequest request) {
        Client client = Client.builder()
                .firstName(request.getFirstName())
                .middleName(request.getMiddleName())
                .lastName(request.getLastName())
                .documentType(request.getDocumentType())
                .documentId(request.getDocumentId())
                .userId(System.currentTimeMillis()) // генерация userId
                .build();
        return clientRepository.save(client);
    }

    public Optional<Client> findById(Long clientId) {
        return clientRepository.findById(clientId);
    }
}
