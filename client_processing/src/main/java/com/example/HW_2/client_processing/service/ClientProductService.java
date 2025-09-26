package com.example.HW2.client_processing.service;

import com.example.HW2.client_processing.dto.ClientProductDto;
import com.example.HW2.client_processing.entity.ClientProduct;
import com.example.HW2.client_processing.repository.ClientProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientProductService {

    private final ClientProductRepository repository;

    public ClientProduct createClientProduct(ClientProductDto dto) {
        ClientProduct clientProduct = ClientProduct.builder()
                .clientId(dto.getClientId())
                .productId(dto.getProductId())
                .build();
        return repository.save(clientProduct);
    }

    public List<ClientProductDto> getByClientId(Long clientId) {
        return repository.findByClientId(clientId).stream()
                .map(ClientProductDto::fromEntity)
                .collect(Collectors.toList());
    }
}
