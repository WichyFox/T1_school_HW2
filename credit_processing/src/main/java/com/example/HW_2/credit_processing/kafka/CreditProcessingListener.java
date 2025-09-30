package com.example.HW_2.credit_processing.kafka;

import com.example.HW_2.credit_processing.dto.ClientInfoDto;
import com.example.HW_2.credit_processing.dto.CreditRequestDto;
import com.example.HW_2.credit_processing.dto.CreditResponseDto;
import com.example.HW_2.credit_processing.entity.PaymentRegistry;
import com.example.HW_2.credit_processing.entity.ProductRegistry;
import com.example.HW_2.credit_processing.service.ClientInfoService;
import com.example.HW_2.credit_processing.service.CreditProcessingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreditProcessingListener {

    private final CreditProcessingService creditProcessingService;
    private final ClientInfoService clientInfoService;
    private final CreditResponseProducer responseProducer;
    private final ObjectMapper objectMapper;

    @Value("${credit.limit}")
    private BigDecimal creditLimit;

    @KafkaListener(topics = "client_credit_products", groupId = "credit_processing")
    public void handleCreditProductRequest(String message) {
        try {
            CreditRequestDto request = objectMapper.readValue(message, CreditRequestDto.class);
            ClientInfoDto clientInfo = clientInfoService.getClientInfo(request.getClientId());

            // Проверка лимита
            BigDecimal currentDebt = creditProcessingService.getClientProducts(request.getClientId())
                    .stream()
                    .map(ProductRegistry::getInterestRate) // можно заменить на сумму кредита, если есть поле суммы
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            if (currentDebt.add(request.getAmount()).compareTo(creditLimit) > 0) {
                log.warn("Кредит отклонён: превышен лимит для клиента {}", clientInfo.getLastName());
                responseProducer.sendResponse(
                        new CreditResponseDto(request.getClientId(), request.getProductId(), false,
                                "Отказ: превышен кредитный лимит")
                );
                return;
            }

            // Проверка просрочек
            boolean hasOverdue = creditProcessingService.getClientProducts(request.getClientId()).stream()
                    .flatMap(p -> creditProcessingService.getPayments(p.getId()).stream())
                    .anyMatch(PaymentRegistry::getExpired);

            if (hasOverdue) {
                log.warn("Кредит отклонён: есть просрочки для клиента {}", clientInfo.getLastName());
                responseProducer.sendResponse(
                        new CreditResponseDto(request.getClientId(), request.getProductId(), false,
                                "Отказ: есть просрочки по кредитам")
                );
                return;
            }

            // Создание кредитного продукта
            creditProcessingService.createProductRegistry(
                    request.getClientId(),
                    request.getProductId(),
                    request.getAmount(),
                    request.getInterestRate(),
                    request.getMonthCount()
            );

            log.info("Кредит одобрен для клиента {} {}", clientInfo.getFirstName(), clientInfo.getLastName());

            responseProducer.sendResponse(
                    new CreditResponseDto(request.getClientId(), request.getProductId(), true,
                            "Кредит одобрен")
            );

        } catch (Exception e) {
            log.error("Ошибка обработки кредитного запроса: {}", e.getMessage(), e);
        }
    }
}
