package com.example.HW2.credit_processing.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreditResponseDto {
    private Long clientId;
    private Long productId;
    private boolean approved;
    private String message;
}
