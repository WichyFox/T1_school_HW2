package com.example.HW_2.credit_processing.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRegistryDto {
    private Long id;
    private Long clientId;
    private Long accountId;
    private Long productId;
    private BigDecimal interestRate;
    private LocalDate openDate;
}
