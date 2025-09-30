package com.example.HW_2.client_processing.dto;

import com.example.HW_2.client_processing.enums.DocumentType;
import lombok.Data;

@Data
public class ClientRegistrationRequest {
    private String login;
    private String password;
    private String email;
    private String firstName;
    private String middleName;
    private String lastName;
    private DocumentType documentType;
    private String documentId;
}
