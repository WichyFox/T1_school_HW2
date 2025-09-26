package com.example.HW2.client_processing.dto;

import com.example.HW2.client_processing.entity.Client;
import com.example.HW2.client_processing.enums.DocumentType;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientDto {

    private Long id;
    private String clientId;
    private Long userId;
    private String login;
    private String firstName;
    private String middleName;
    private String lastName;
    private LocalDate dateOfBirth;
    private DocumentType documentType;
    private String documentId;
    private String documentPrefix;
    private String documentSuffix;

    // Маппинг из entity в DTO
    public static ClientDto fromEntity(Client client) {
        return ClientDto.builder()
                .id(client.getId())
                .clientId(client.getClientId())
                .userId(client.getUserId())
                .firstName(client.getFirstName())
                .middleName(client.getMiddleName())
                .lastName(client.getLastName())
                .dateOfBirth(client.getDateOfBirth())
                .documentType(client.getDocumentType())
                .documentId(client.getDocumentId())
                .documentPrefix(client.getDocumentPrefix())
                .documentSuffix(client.getDocumentSuffix())
                .build();
    }
}
