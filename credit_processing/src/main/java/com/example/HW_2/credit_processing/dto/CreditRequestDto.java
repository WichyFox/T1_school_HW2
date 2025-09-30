package com.example.HW_2.credit_processing.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreditRequestDto {
    private Long clientId;
    private Long productId;
    private BigDecimal amount;
    private BigDecimal interestRate;
    private Integer monthCount;
}
