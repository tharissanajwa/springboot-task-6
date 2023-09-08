package com.springboot.task6.services;

import com.springboot.task6.model.OrderDetail;
import com.springboot.task6.repositories.OrderDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderDetailService {
    @Autowired
    private OrderDetailRepository repository;
    private String responseMessage;

    public String getResponseMessage() {
        return this.responseMessage;
    }

    public List<OrderDetail> getOrderDetailByOrderId(Long orderId) {
        return repository.getOrderDetailsByOrderIdAndDeletedAtIsNull(orderId);
    }
}
