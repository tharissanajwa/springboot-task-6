package com.springboot.task6.services;

import com.springboot.task6.model.Member;
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

    // Metode untuk mengambil semua data pembayaran
    public List<Payment> getAllPayment() {
        List<Payment> result = paymentRepository.findAllByDeletedAtIsNullOrderByIdDesc();
        if (result.isEmpty()) {
            responseMessage = "Data doesn't exists, please insert new data payment.";
        } else {
            responseMessage = "Data successfully loaded.";
        }
        return result;
    }

    // Metode untuk mengambil data pembayaran berdasarkan id
    public Payment getPaymentById(Long id) {
        Optional<Payment> result = paymentRepository.findByIdAndDeletedAtIsNull(id);
        if (result.isPresent()) {
            responseMessage = "Data successfully loaded.";
            return result.get();
        }
        responseMessage = "Sorry, id payment is not found.";
        return null;
    }

    // Metode untuk membuat pembayaran baru
    public Payment insertPayment(Integer totalPaid, Long orderId, Integer pointUsed) {
        if (!isPaymentValid(orderId).isEmpty()) {
            responseMessage = isPaymentValid(orderId);
            return null;
        } else {
            Order order = orderService.getOrderById(orderId);
            Integer discount = calculateDiscount(orderId, pointUsed);
            Integer change = calculateChange(totalPaid, order.getTotalAmount(), discount);

            if (change == null) {
                return null;
            } else if (order.getOrderDetails().isEmpty()) {
                responseMessage = "Sorry, order with ID " + orderId + " doesn't have any product yet.";
                return null;
            } else {
                Payment result = new Payment(totalPaid, discount, order, pointUsed);
                result.setChange(change);
                result.setCreatedAt(new Date());
                result.setDiscount(discount);
                paymentRepository.save(result);

                int pointToAdd = order.getPointObtained();
                if (pointUsed == null || pointUsed == 0) {
                    order.getMember().addPoints(pointToAdd);
                }
                order.setIsPaid(true);
                TableOrder tableOrder = order.getTable();
                tableOrder.setAvailable(true);
                orderRepository.save(order);

                responseMessage = "Payment successfully added!";
                return result;
            }
        }
    }

    // Metode untuk mengubah pembayaran
    public Payment updatePayment(Integer totalPaid, Long orderId, Integer pointUsed) {
        Order order = orderService.getOrderById(orderId);
        if (order == null) {
            responseMessage = "Sorry, order is not found.";
            return null;
        } else {
            Integer discount = calculateDiscount(orderId, pointUsed);
            Integer change = calculateChange(totalPaid, order.getTotalAmount(), discount);

            if (discount == null) {
                return null;
            } else if (change == null) {
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

    // Metode untuk menghapus pembayaran
    public boolean deletePayment(Long paymentId) {
        Payment payment = getPaymentById(paymentId);
        if (payment != null) {
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
            paymentRepository.deleteById(paymentId);

            responseMessage = "Payment deleted successfully.";
            return true;
        } else {
            responseMessage = "Payment not found.";
            return false;
        }
    }

    // Metode untuk validasi pembayaran
    private String isPaymentValid(Long orderId) {
        String result = "";
        Order order = orderService.getOrderById(orderId);

        if (order == null) {
            result = "Sorry, order is not found.";
        } else {
            boolean isDone = true;
            for (OrderDetail orderDetail : order.getOrderDetails()) {
                if (!orderDetail.getDone()) {
                    isDone = false;
                    break;
                }
            }

            if (order.isPaid()) {
                result = "Payment cannot be made because the order has been paid.";
            } else if (!isDone) {
                result = "Payment cannot be made because not all order details are done.";
            }
        }
        return result;
    }

    // Metode untuk menghitung diskon dari point member yang tersedia untuk insert payment
    private Integer calculateDiscount(Long orderId, Integer pointUsed) {
        Order order = orderService.getOrderById(orderId);
        Member member = order.getMember();
        Integer discount = null;

        if (order.getMember() != null && pointUsed != null) {
            if (order.getMember().getPoint() == 0) {
                responseMessage = "Sorry, member " + member.getName() + " don't have any points.";
            } else if (pointUsed > 50) {
                responseMessage = "Sorry, maximum point to use is 50.";
            } else if (pointUsed > member.getPoint()) {
                responseMessage = "Sorry, member " + member.getName() + " only have " + member.getPoint() + " points.";
            } else {
                int totalToPointConversion = order.getTotalAmount() / 10;

                if (pointUsed > order.getTotalAmount()) {
                    responseMessage = "Sorry, point to use is maxed at " + totalToPointConversion + ".";
                } else if (pointUsed < order.getTotalAmount()) {
                    int difference = order.getTotalAmount() - pointUsed;
                    responseMessage = "Sorry, your points conversion is " + pointUsed + " and total to pay is " + order.getTotalAmount() + ". Please pay " + difference + " to cashier.";
                } else {
                    discount = pointUsed * 1000;
                    member.minusPoints(pointUsed);
                }
            }
        } else {
            discount = 0;
        }

        return discount;
    }

    // Metode untuk menghitung kembalian dari total yang sudah dibayarkan
    private Integer calculateChange(Integer totalPaid, Integer totalAmount, Integer discount) {
        Integer result = null;

        if (discount != null) {
            int totals = totalAmount - discount;
            if (totalPaid < totals) {
                responseMessage = "Sorry, the total paid must exceed the total amount.";
            } else {
                result = totalPaid - totals;
            }
        }
        return result;
    }
}