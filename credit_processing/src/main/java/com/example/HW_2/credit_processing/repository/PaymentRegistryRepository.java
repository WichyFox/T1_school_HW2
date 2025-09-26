package com.example.HW2.credit_processing.repository;

import com.example.HW2.credit_processing.entity.PaymentRegistry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRegistryRepository extends JpaRepository<PaymentRegistry, Long> {
    List<PaymentRegistry> findByProductRegistryId(Long productRegistryId);
}
