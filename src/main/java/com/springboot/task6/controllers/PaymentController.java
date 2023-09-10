package com.springboot.task6.controllers;

import com.springboot.task6.model.ApiResponse;
import com.springboot.task6.model.Payment;
import com.springboot.task6.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payments")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    // Metode untuk mengambil semua data pembayaran dari fungsi yg telah dibuat di service
    @GetMapping("")
    public ResponseEntity<ApiResponse> getAllPayment() {
        List<Payment> payments = paymentService.getAllPayment();
        ApiResponse response = new ApiResponse(paymentService.getResponseMessage(), payments);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // Metode untuk mengambil data pembayaran berdasarkan id dari fungsi yg telah dibuat di service
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getPaymentById(@PathVariable("id") Long id) {
        Payment payments = paymentService.getPaymentById(id);
        ApiResponse response = new ApiResponse(paymentService.getResponseMessage(), payments);
        HttpStatus httpStatus;

        if (payments != null) httpStatus = HttpStatus.OK;
        else httpStatus = HttpStatus.BAD_REQUEST;

        return ResponseEntity.status(httpStatus).body(response);
    }

    // Metode untuk membuat pembayaran baru dari fungsi yg telah dibuat di service
    @PostMapping("/{orderId}")
    public ResponseEntity<ApiResponse> insertPayment(@PathVariable("orderId") Long orderId, @RequestBody Payment payment) {
        Payment payments = paymentService.insertPayment(payment.getTotalPaid(), orderId);
        ApiResponse response = new ApiResponse(paymentService.getResponseMessage(), payments);
        HttpStatus httpStatus;

        if (payments != null) httpStatus = HttpStatus.OK;
        else httpStatus = HttpStatus.BAD_REQUEST;

        return  ResponseEntity.status(httpStatus).body(response);
    }

    // Metode untuk membuat pembayaran baru dari fungsi yg telah dibuat di service
    @PutMapping("/{orderId}")
    public ResponseEntity<ApiResponse> updatePayment(@PathVariable("orderId") Long orderId, @RequestBody Payment payment) {
        Payment payments = paymentService.updatePayment(payment.getTotalPaid(), orderId);
        ApiResponse response = new ApiResponse(paymentService.getResponseMessage(), payments);
        HttpStatus httpStatus;

        if (payments != null) httpStatus = HttpStatus.OK;
        else httpStatus = HttpStatus.BAD_REQUEST;

        return  ResponseEntity.status(httpStatus).body(response);
    }

    // Metode untuk menghapus pembayaran berdasarkan dari fungsi yg telah dibuat di service
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deletePayment(@PathVariable("id") Long id) {
        boolean isValid = paymentService.deletePayment(id);
        ApiResponse response = new ApiResponse(paymentService.getResponseMessage(), null);
        HttpStatus httpStatus;

        if (isValid) httpStatus = HttpStatus.OK;
        else httpStatus = HttpStatus.BAD_REQUEST;

        return ResponseEntity.status(httpStatus).body(response);
    }
}