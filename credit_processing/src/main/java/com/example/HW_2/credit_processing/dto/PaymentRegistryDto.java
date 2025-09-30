package com.example.HW_2.credit_processing.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class PaymentRegistryDto {
    private Long id;
    private Long productRegistryId;
    private LocalDate paymentDate;
    private BigDecimal amount;
    private BigDecimal interestRateAmount;
    private BigDecimal debtAmount;
    private Boolean expired;
    private LocalDate paymentExpirationDate;
}