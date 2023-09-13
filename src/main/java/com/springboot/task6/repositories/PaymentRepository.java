package com.springboot.task6.repositories;

import com.springboot.task6.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findAllByDeletedAtIsNullOrderByIdDesc();
    Optional<Payment> findByIdAndDeletedAtIsNull(Long id);
}
