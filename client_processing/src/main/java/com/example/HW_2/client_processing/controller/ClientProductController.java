package com.example.HW_2.client_processing.controller;

import com.example.HW_2.client_processing.dto.ClientProductDto;
import com.example.HW_2.client_processing.entity.ClientProduct;
import com.example.HW_2.client_processing.kafka.KafkaProducer;
import com.example.HW_2.client_processing.service.ClientProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/client-products")
@RequiredArgsConstructor
public class ClientProductController {

    private final ClientProductService clientProductService;
    private final KafkaProducer kafkaProducer;

    @PostMapping
    public ResponseEntity<ClientProductDto> addClientProduct(@RequestBody ClientProductDto dto) {
        ClientProduct clientProduct = clientProductService.createClientProduct(dto);
        // по ТЗ: DC, CC, NS, PENS -> client_products
        switch (clientProduct.getProductId().toString()) {
            case "DC", "CC", "NS", "PENS" -> kafkaProducer.sendToClientProducts(
                    clientProduct.getClientId() + "," + clientProduct.getProductId()
            );
            case "IPO", "PC", "AC" -> kafkaProducer.sendToClientCreditProducts(
                    clientProduct.getClientId() + "," + clientProduct.getProductId() + ",1000000,0.22,60"
            );
        }

        return ResponseEntity.ok(ClientProductDto.fromEntity(clientProduct));
    }

    @GetMapping("/{clientId}")
    public ResponseEntity<List<ClientProductDto>> getClientProducts(@PathVariable Long clientId) {
        return ResponseEntity.ok(clientProductService.getByClientId(clientId));
    }
}
