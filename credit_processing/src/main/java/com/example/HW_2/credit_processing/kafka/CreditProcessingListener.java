package com.example.HW2.credit_processing.kafka;

import com.example.HW2.credit_processing.dto.ClientInfoDto;
import com.example.HW2.credit_processing.dto.CreditRequestDto;
import com.example.HW2.credit_processing.dto.CreditResponseDto;
import com.example.HW2.credit_processing.service.ClientInfoService;
import com.example.HW2.credit_processing.service.CreditProcessingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreditProcessingListener {

    private final CreditProcessingService creditProcessingService;
    private final ClientInfoService clientInfoService;
    private final CreditResponseProducer responseProducer;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "client_credit_products", groupId = "credit-processing")
    public void handleCreditProductRequest(String message) {
        try {
            CreditRequestDto request = objectMapper.readValue(message, CreditRequestDto.class);
            ClientInfoDto clientInfo = clientInfoService.getClientInfo(request.getClientId());

            // Проверка лимита
            if (creditProcessingService.isLimitExceeded(request.getClientId(), request.getAmount())) {
                log.warn("Кредит отклонён: превышен лимит для клиента {}", clientInfo.getLastName());
                responseProducer.sendResponse(
                        new CreditResponseDto(request.getClientId(), request.getProductId(), false,
                                "Отказ: превышен кредитный лимит")
                );
                return;
            }

            // Проверка просрочек
            if (creditProcessingService.hasOverduePayments(request.getClientId())) {
                log.warn("Кредит отклонён: есть просрочки для клиента {}", clientInfo.getLastName());
                responseProducer.sendResponse(
                        new CreditResponseDto(request.getClientId(), request.getProductId(), false,
                                "Отказ: есть просрочки по кредитам")
                );
                return;
            }

            // Открытие кредита
            creditProcessingService.openCreditProduct(request);
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
