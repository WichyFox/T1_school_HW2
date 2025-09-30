package com.example.HW_2.client_processing.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendToClientProducts(String message) {
        log.info("📤 Отправка в client_products: {}", message);
        kafkaTemplate.send("client_products", message);
    }

    public void sendToClientCreditProducts(String message) {
        log.info("📤 Отправка в client_credit_products: {}", message);
        kafkaTemplate.send("client_credit_products", message);
    }

    public void sendToClientCards(String message) {
        log.info("📤 Отправка в client_cards: {}", message);
        kafkaTemplate.send("client_cards", message);
    }
}
