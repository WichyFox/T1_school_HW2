package com.example.HW2.credit_processing.repository;

import com.example.HW2.credit_processing.entity.ProductRegistry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRegistryRepository extends JpaRepository<ProductRegistry, Long> {
    List<ProductRegistry> findByClientId(Long clientId);
}
