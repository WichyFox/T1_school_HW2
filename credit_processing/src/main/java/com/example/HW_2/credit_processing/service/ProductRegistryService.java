package com.example.HW_2.credit_processing.service;

import com.example.HW_2.credit_processing.entity.ProductRegistry;
import com.example.HW_2.credit_processing.repository.ProductRegistryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductRegistryService {
    private final ProductRegistryRepository productRegistryRepository;

    public List<ProductRegistry> findAll() {
        return productRegistryRepository.findAll();
    }

    public Optional<ProductRegistry> findById(Long id) {
        return productRegistryRepository.findById(id);
    }

    public ProductRegistry save(ProductRegistry registry) {
        return productRegistryRepository.save(registry);
    }

    public void delete(Long id) {
        productRegistryRepository.deleteById(id);
    }
}
