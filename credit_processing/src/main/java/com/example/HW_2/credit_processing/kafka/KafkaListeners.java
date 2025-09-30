package com.example.HW_2.credit_processing.kafka;

import com.example.HW_2.credit_processing.entity.ProductRegistry;
import com.example.HW_2.credit_processing.entity.PaymentRegistry;
import com.example.HW_2.credit_processing.repository.ProductRegistryRepository;
import com.example.HW_2.credit_processing.repository.PaymentRegistryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class KafkaListeners {

    private final ProductRegistryRepository productRegistryRepository;
    private final PaymentRegistryRepository paymentRegistryRepository;

    @Value("${credit.limit}")
    private BigDecimal creditLimit;

    @KafkaListener(topics = "client_credit_products", groupId = "credit-service")
    public void consumeCreditProducts(String message) {
        //clientId,productId,sum,interestRate,monthCount
        String[] parts = message.split(",");
        Long clientId = Long.valueOf(parts[0]);
        Long productId = Long.valueOf(parts[1]);
        BigDecimal sum = new BigDecimal(parts[2]);
        BigDecimal interestRate = new BigDecimal(parts[3]);
        int monthCount = Integer.parseInt(parts[4]);

        // 1) Проверка клиента через МС-1
        RestTemplate restTemplate = new RestTemplate();
        String clientInfo = restTemplate.getForObject(
                "http://localhost:8081/api/clients/" + clientId, String.class);
        System.out.println("👤 Got client info: " + clientInfo);

        // 2) Проверка лимита
        BigDecimal currentDebt = productRegistryRepository.findAll().stream()
                .filter(p -> p.getClientId().equals(clientId))
                .map(p -> p.getInterestRate()) // упрощённо считаем задолженность
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (currentDebt.add(sum).compareTo(creditLimit) > 0) {
            System.out.println("❌ Credit denied: limit exceeded");
            return;
        }

        // 3) Создание ProductRegistry
        ProductRegistry productRegistry = ProductRegistry.builder()
                .clientId(clientId)
                .productId(productId)
                .interestRate(interestRate)
                .openDate(LocalDate.now())
                .monthCount(monthCount)
                .build();
        productRegistryRepository.save(productRegistry);

        // 4) Создание графика платежей
        BigDecimal monthlyPayment = calculateAnnuity(sum, interestRate, monthCount);
        BigDecimal remainingDebt = sum;

        for (int i = 1; i <= monthCount; i++) {
            BigDecimal interest = remainingDebt.multiply(interestRate.divide(BigDecimal.valueOf(12), 10, BigDecimal.ROUND_HALF_UP));
            BigDecimal principal = monthlyPayment.subtract(interest);
            remainingDebt = remainingDebt.subtract(principal);

            PaymentRegistry payment = PaymentRegistry.builder()
                    .productRegistryId(productRegistry.getId())
                    .paymentDate(LocalDate.now().plusMonths(i))
                    .amount(monthlyPayment)
                    .interestRateAmount(interest)
                    .debtAmount(principal)
                    .expired(false)
                    .paymentExpirationDate(LocalDate.now().plusMonths(i))
                    .build();

            paymentRegistryRepository.save(payment);
        }

        System.out.println("Credit approved for client " + clientId);
    }

    private BigDecimal calculateAnnuity(BigDecimal sum, BigDecimal annualRate, int months) {
        BigDecimal monthlyRate = annualRate.divide(BigDecimal.valueOf(12), 10, BigDecimal.ROUND_HALF_UP);
        BigDecimal numerator = monthlyRate.multiply((BigDecimal.ONE.add(monthlyRate)).pow(months));
        BigDecimal denominator = (BigDecimal.ONE.add(monthlyRate)).pow(months).subtract(BigDecimal.ONE);
        return sum.multiply(numerator.divide(denominator, 2, BigDecimal.ROUND_HALF_UP));
    }
}
