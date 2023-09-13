package com.springboot.task6.repositories;

import com.springboot.task6.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByDeletedAtIsNullOrderByIdDesc();
    Optional<Order> findByIdAndDeletedAtIsNull(Long id);
    List<Order> findAllByDeletedAtIsNullAndIsPaidTrueOrderByIdDesc();
}
