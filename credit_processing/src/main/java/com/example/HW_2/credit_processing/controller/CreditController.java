package com.example.HW_2.credit_processing.controller;

import com.example.HW_2.credit_processing.entity.ProductRegistry;
import com.example.HW_2.credit_processing.service.CreditProcessingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class CreditController {
    private final CreditProcessingService creditProcessingService;

    @PostMapping("/open")
    public ResponseEntity<ProductRegistry> openCredit(
            @RequestParam Long clientId,
            @RequestParam Long productId,
            @RequestParam BigDecimal sum,
            @RequestParam BigDecimal interestRate,
            @RequestParam int monthCount
    ) {
        ProductRegistry registry = creditProcessingService.createProductRegistry(clientId, productId, sum, interestRate, monthCount);
        return ResponseEntity.ok(registry);
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<ProductRegistry>> getClientCredits(@PathVariable Long clientId) {
        return ResponseEntity.ok(creditProcessingService.getClientProducts(clientId));
    }
}
