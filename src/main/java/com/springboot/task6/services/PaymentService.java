package com.springboot.task6.services;

import com.springboot.task6.model.Order;
import com.springboot.task6.model.Payment;
import com.springboot.task6.repositories.OrderRepository;
import com.springboot.task6.repositories.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderDetailService orderDetailService;

    private String responseMessage; // Pesan status untuk memberi informasi kepada pengguna

    // Metode untuk mendapatkan pesan status
    public String getResponseMessage() {
        return responseMessage;
    }

    // Metode untuk mendapatkan semua daftar transaksi yang belum terhapus melalui repository
    public List<Payment> getAllPayment() {
        if (paymentRepository.findAllByDeletedAtIsNull().isEmpty()) {
            responseMessage = "Data doesn't exists, please insert new data order.";
        } else {
            responseMessage = "Data successfully loaded.";
        }
        return paymentRepository.findAllByDeletedAtIsNull();
    }

    // Metode untuk mendapatkan data transaksi berdasarkan id melalui repository
    public Payment getPaymentById(Long id) {
        Optional<Payment> result = paymentRepository.findByIdAndDeletedAtIsNull(id);
        if (result.isPresent()) {
            responseMessage = "Data successfully loaded.";
            return result.get();
        }
        responseMessage = "Sorry, id order is not found.";
        return null;
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

        boolean valid = false;
        int number = 0;
        while (!valid) {
            if (!order.getOrderDetails().get(number-1).isDone()){
                valid = true;
            }
            number++;
        }

        if (valid) {
            responseMessage = "Payment cannot be made because not all order details are done.";
            return false;
        }

        return true;
    }

    public Payment insertPayment(Integer totalPaid, Integer discount, Long orderId) {
        if (!isPaymentValid(orderId)) {
            return null;
        }

        Payment result = new Payment(totalPaid, discount, orderService.getOrderById(orderId));
        result.setChange(totalPaid-(orderService.getOrderById(orderId).getTotalAmount()-discount));
        result.setCreatedAt(new Date());
        paymentRepository.save(result);

        Order order = result.getOrder();
        order.setIsPaid(true);
        orderRepository.save(order);

        responseMessage = "Payment successfully added!";
        return result;
    }

    public Payment updatePayment(Integer totalPaid, Integer discount, Long orderId) {
        Optional<Payment> optionalPayment = paymentRepository.findById(orderId);

        if (optionalPayment.isPresent()) {
            Payment payment = optionalPayment.get();

            payment.setTotalPaid(totalPaid);
            payment.setDiscount(discount);
            payment.setChange(totalPaid-(orderService.getOrderById(orderId).getTotalAmount()-discount));

            paymentRepository.save(payment);

            responseMessage = "Payment updated successfully.";
            return payment;
        } else {
            responseMessage = "Payment not found.";
            return null;
        }
    }

    public boolean deletePayment(Long paymentId) {
        Optional<Payment> optionalPayment = paymentRepository.findById(paymentId);

        if (optionalPayment.isPresent()) {
            Payment payment = optionalPayment.get();

            payment.setDeletedAt(new Date());
            paymentRepository.save(payment);

            responseMessage = "Payment deleted successfully.";
            return true;
        } else {
            responseMessage = "Payment not found.";
            return false;
        }
    }
}
