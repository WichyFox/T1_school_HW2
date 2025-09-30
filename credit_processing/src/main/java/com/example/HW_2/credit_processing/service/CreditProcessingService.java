package com.example.HW_2.credit_processing.service;

import com.example.HW_2.credit_processing.dto.CreditRequestDto;
import com.example.HW_2.credit_processing.entity.PaymentRegistry;
import com.example.HW_2.credit_processing.entity.ProductRegistry;
import com.example.HW_2.credit_processing.repository.PaymentRegistryRepository;
import com.example.HW_2.credit_processing.repository.ProductRegistryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CreditProcessingService
{

    private final ProductRegistryRepository productRegistryRepository;
    private final PaymentRegistryRepository paymentRegistryRepository;
    public List<PaymentRegistry> getPayments(Long productRegistryId) {
        return paymentRegistryRepository.findByProductRegistryId(productRegistryId);
    }
    public ProductRegistry createProductRegistry(Long clientId, Long productId, BigDecimal sum, BigDecimal interestRate, int monthCount)
    {
        ProductRegistry registry = ProductRegistry.builder()
                .clientId(clientId)
                .productId(productId)
                .interestRate(interestRate)
                .openDate(LocalDate.now())
                .monthCount(monthCount)
                .build();
        productRegistryRepository.save(registry); // Генерация графика платежей
        BigDecimal monthlyPayment = calculateAnnuity(sum, interestRate, monthCount);
        BigDecimal remainingDebt = sum;
        for (int i = 1; i <= monthCount; i++)
        {
            BigDecimal interest = remainingDebt.multiply(interestRate.divide(BigDecimal.valueOf(12), 10, BigDecimal.ROUND_HALF_UP));
            BigDecimal principal = monthlyPayment.subtract(interest);
            remainingDebt = remainingDebt.subtract(principal);
            PaymentRegistry payment = PaymentRegistry.builder()
                    .productRegistryId(registry.getId())
                    .paymentDate(LocalDate.now().plusMonths(i))
                    .amount(monthlyPayment)
                    .interestRateAmount(interest)
                    .debtAmount(principal)
                    .expired(false)
                    .paymentExpirationDate(LocalDate.now().plusMonths(i))
                    .build();
            paymentRegistryRepository.save(payment);
        }
        return registry;
    }
    private BigDecimal calculateAnnuity(BigDecimal sum, BigDecimal annualRate, int months)
    {
        BigDecimal monthlyRate = annualRate.divide(BigDecimal.valueOf(12), 10, BigDecimal.ROUND_HALF_UP);
        BigDecimal numerator = monthlyRate.multiply((BigDecimal.ONE.add(monthlyRate)).pow(months));
        BigDecimal denominator = (BigDecimal.ONE.add(monthlyRate)).pow(months).subtract(BigDecimal.ONE);
        return sum.multiply(numerator.divide(denominator, 2, BigDecimal.ROUND_HALF_UP));
    }
    public List<ProductRegistry> getClientProducts(Long clientId)
    {
        return productRegistryRepository.findByClientId(clientId);
    }
}
