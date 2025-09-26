package com.example.HW2.credit_processing.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientInfoDto {
    private String firstName;
    private String lastName;
    private String documentId;
}
