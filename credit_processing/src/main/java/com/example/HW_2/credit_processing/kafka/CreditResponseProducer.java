package com.example.HW2.credit_processing.kafka;

import com.example.HW2.credit_processing.dto.CreditResponseDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreditResponseProducer {

    private static final String TOPIC = "credit_responses";

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void sendResponse(CreditResponseDto response) {
        try {
            String json = objectMapper.writeValueAsString(response);
            kafkaTemplate.send(TOPIC, json);
            log.info("Отправлен ответ в Kafka: {}", json);
        } catch (JsonProcessingException e) {
            log.error("Ошибка сериализации ответа: {}", e.getMessage(), e);
        }
    }
}
