package com.example.shop.repository;

import com.example.shop.domain.shop.Payments;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payments, Long> {
    Optional<Payments> findByOrderId(Long orderId);
}
