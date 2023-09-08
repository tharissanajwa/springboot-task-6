package com.springboot.task6.services;

import com.springboot.task6.model.Order;
import com.springboot.task6.model.OrderDetail;
import com.springboot.task6.model.Payment;
import com.springboot.task6.repositories.OrderRepository;
import com.springboot.task6.repositories.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderService orderService;

    private String responseMessage; // Pesan status untuk memberi informasi kepada pengguna

    // Metode untuk mendapatkan pesan status
    public String getResponseMessage() {
        return responseMessage;
    }

    public boolean isPaymentValid(Long orderId) {
        Order order = orderService.getOrderById(orderId);

        if (order == null) {
            responseMessage = "Order not found.";
            return false;
        }

        if (order.getIsPaid()) {
            responseMessage = "Payment cannot be made because the order is already paid.";
            return false;
        }

        for (OrderDetail orderDetail : order.getOrderDetails()) {
            if (!orderDetail.isDone()) {
                responseMessage = "Payment cannot be made because not all order details are done.";
                return false;
            }
        }

        return true;
    }

    public Payment insertPayment(Integer totalPaid, Integer discount, Long orderId) {
        Order order = orderService.getOrderById(orderId);

        if (order == null) {
            responseMessage = "Order not found.";
            return null;
        }

        if (!isPaymentValid(orderId)) {
            return null;
        }

        Integer change = calculateChange(totalPaid, order.getTotalAmount(), discount);

        Payment result = new Payment(totalPaid, discount, order);
        result.setChange(change);

        result.setCreatedAt(new Date());
        paymentRepository.save(result);

        order.setIsPaid(true);
        orderRepository.save(order);

        responseMessage = "Payment successfully added!";
        return result;
    }

    public boolean deletePayment(Long paymentId) {
        Optional<Payment> optionalPayment = paymentRepository.findById(paymentId);

        if (optionalPayment.isPresent()) {
            optionalPayment.get().setDeletedAt(new Date());
            paymentRepository.save(optionalPayment.get());

            responseMessage = "Payment deleted successfully.";
            return true;
        } else {
            responseMessage = "Payment not found.";
            return false;
        }
    }

    public Integer calculateChange(Integer totalPaid, Integer totalAmount, Integer discount) {
        return totalPaid - (totalAmount - discount);
    }
}