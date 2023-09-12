package com.springboot.task6.repositories;

import com.springboot.task6.model.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    List<OrderDetail> getOrderDetailsByOrderIdAndDeletedAtIsNullOrderById(Long orderId);
}
