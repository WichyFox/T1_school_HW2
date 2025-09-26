package com.example.HW2.client_processing.controller;

import com.example.HW2.client_processing.dto.CardRequestDto;
import com.example.HW2.client_processing.kafka.KafkaProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cards")
@RequiredArgsConstructor
public class CardController {

    private final KafkaProducer kafkaProducer;

    @PostMapping
    public ResponseEntity<Void> createCard(@RequestBody CardRequestDto dto) {
        // Отправка запроса в Kafka
        kafkaProducer.sendToClientCards(dto.getAccountId() + "," + dto.getCardId());
        return ResponseEntity.ok().build();
    }
}
