package com.springboot.task6.services;

import com.springboot.task6.model.Order;
import com.springboot.task6.model.OrderDetail;
import com.springboot.task6.model.Payment;
import com.springboot.task6.model.TableOrder;
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

    private String responseMessage; // Pesan status untuk memberi informasi kepada pengguna

    // Metode untuk mendapatkan pesan status
    public String getResponseMessage() {
        return responseMessage;
    }

    public List<Payment> getAllPayment() {
        if (paymentRepository.findAllByDeletedAtIsNull().isEmpty()) {
            responseMessage = "Data doesn't exists, please insert new data payment.";
        } else {
            responseMessage = "Data successfully loaded.";
        }
        return paymentRepository.findAllByDeletedAtIsNull();
    }

    public Payment getPaymentById(Long id) {
        Optional<Payment> result = paymentRepository.findByIdAndDeletedAtIsNull(id);
        if (result.isPresent()) {
            responseMessage = "Data successfully loaded.";
            return result.get();
        }
        responseMessage = "Sorry, id payment is not found.";
        return null;
    }

    public Payment insertPayment(Integer totalPaid, Long orderId) {
        if (!isPaymentValid(orderId).isEmpty()) {
            responseMessage = isPaymentValid(orderId);
            return null;
        } else {
            Order order = orderService.getOrderById(orderId);
            int point = order.getPointObtained();
            int discount = 0;
            if (order.getMember() != null) {
                order.getMember().addPoints(point);
                if (order.getMember().getPoint() > 100) {
                    discount = point * 100;
                    order.getMember().minusPoints(point);
                }
            }
            Integer change = calculateChange(totalPaid, order.getTotalAmount(), discount);
            if (change == null) {
                return null;
            } else if (order.getOrderDetails().isEmpty()) {
                responseMessage = "Sorry, order with ID " + orderId + " doesn't have any product yet.";
                return null;
            } else {
                Payment result = new Payment(totalPaid, discount, order);
                result.setChange(change);
                result.setCreatedAt(new Date());
                paymentRepository.save(result);

                order.setIsPaid(true);
                TableOrder tableOrder = order.getTable();
                tableOrder.setAvailable(true);

                orderRepository.save(order);

                responseMessage = "Payment successfully added!";
                return result;
            }
        }
    }

    public Payment updatePayment(Integer totalPaid, Long orderId) {
        Order order = orderService.getOrderById(orderId);
        if (order == null) {
            responseMessage = "Sorry, order is not found.";
            return null;
        } else {
            int point = order.getPointObtained();
            int discount = 0;
            if (order.getMember() != null) {
                if (order.getMember().getPoint() > 100) {
                    discount = point * 100;
                }
            }
            Integer change = calculateChange(totalPaid, order.getTotalAmount(), discount);
            if (change == null) {
                return null;
            } else {
                Payment result = getPaymentById(order.getPayment().getId());
                result.setTotalPaid(totalPaid);
                result.setChange(change);
                result.setUpdatedAt(new Date());
                paymentRepository.save(result);

                order.setIsPaid(true);
                orderRepository.save(order);

                responseMessage = "Payment successfully updated!";
                return result;
            }
        }
    }

    public boolean deletePayment(Long paymentId) {
        Payment payment = getPaymentById(paymentId);
        if (payment != null) {
            paymentRepository.deleteById(paymentId);

            Order order = orderService.getOrderById(payment.getOrder().getId());
            int point = order.getPointObtained();
            if (order.getMember() != null) {
                if (order.getPointObtained() <= 100) {
                    order.getMember().minusPoints(point);
                }
                if (order.getMember().getPoint() > 100) {
                    order.getMember().addPoints(point);
                }
            }

            order.setIsPaid(false);
            TableOrder tableOrder = order.getTable();
            tableOrder.setAvailable(false);

            orderRepository.save(order);

            responseMessage = "Payment deleted successfully.";
            return true;
        } else {
            responseMessage = "Payment not found.";
            return false;
        }
    }

    public Integer calculateChange(Integer totalPaid, Integer totalAmount, Integer discount) {
        Integer result = null;
        int totals = totalAmount - discount;
        if (totalPaid < totals) {
            responseMessage = "Sorry, the total paid must exceed the total amount.";
        } else {
            result = totalPaid - totals;
        }
        return result;
    }

    private String isPaymentValid(Long orderId) {
        String result = "";
        Order order = orderService.getOrderById(orderId);

        if (order == null) {
            result = "Sorry, order is not found.";
        } else {
            boolean isDone = true;
            for (OrderDetail orderDetail : order.getOrderDetails()) {
                if (!orderDetail.isDone()) {
                    isDone = false;
                }
            }

            if (order.isPaid()) {
                result = "Payment cannot be made because the order has been paid.";
            } else if (!isDone) {
                result =  "Payment cannot be made because not all order details are done.";
            }
        }
        return result;
    }
}