package com.springboot.task6.services;

import com.springboot.task6.model.Order;
import com.springboot.task6.model.OrderDetail;
import com.springboot.task6.model.Product;
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

    public OrderDetail createOrderDetail(Order order, Product product, int qty) {
        OrderDetail result = null;

        if (qty > 0) {
            if (!order.getIsPaid()) {
                result = new OrderDetail(order, product, qty);
                int totalAmount = 0;

                for (int i = 0; i < order.getOrderDetails().size() - 1; i++) {
                    OrderDetail detail = order.getOrderDetails().get(i);
                    totalAmount += (detail.getProduct().getPrice() * qty);
                }

                order.setTotalAmount(totalAmount);
            }
        } else {
            responseMessage = "Sorry, item quantity must be positive number.";
        }

        return result;
    }
}
