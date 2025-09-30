package com.example.HW_2.client_processing.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendToClientProducts(String message) {
        kafkaTemplate.send("client_products", message);
    }

    public void sendToClientCreditProducts(String message) {
        kafkaTemplate.send("client_credit_products", message);
    }

    public void sendToClientCards(String message) {
        kafkaTemplate.send("client_cards", message);
    }
}
