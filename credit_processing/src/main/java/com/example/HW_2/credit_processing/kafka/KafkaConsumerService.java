package com.example.HW_2.credit_processing.kafka;

import com.example.HW_2.credit_processing.entity.ProductRegistry;
import com.example.HW_2.credit_processing.service.CreditProcessingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaConsumerService {

    private final CreditProcessingService creditService;

    @KafkaListener(topics = "client_credit_products", groupId = "credit_processing")
    public void consumeClientCreditProduct(String message) {
        log.info("Получено сообщение из client_credit_products: {}", message);

        //clientId:productId:sum:rate:months
        try {
            String[] parts = message.split(":");
            Long clientId = Long.valueOf(parts[0]);
            Long productId = Long.valueOf(parts[1]);
            BigDecimal sum = new BigDecimal(parts[2]);
            BigDecimal interestRate = new BigDecimal(parts[3]);
            int months = Integer.parseInt(parts[4]);

            ProductRegistry registry = creditService.createProductRegistry(
                    clientId, productId, sum, interestRate, months
            );

            log.info("Открыт кредитный продукт {} для клиента {}", registry.getId(), clientId);

        } catch (Exception e) {
            log.error("Ошибка при обработке кредитного продукта: {}", e.getMessage());
        }
    }
}
