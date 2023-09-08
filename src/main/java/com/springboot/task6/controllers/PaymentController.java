package com.springboot.task6.controllers;

import com.springboot.task6.model.ApiResponse;
import com.springboot.task6.model.Payment;
import com.springboot.task6.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @PostMapping("")
    public ResponseEntity<ApiResponse> insertPayment(@RequestBody Payment payment) {
        Payment payments = paymentService.insertPayment(payment.getTotalPaid(), payment.getDiscount(), payment.getOrder().getId());
        ApiResponse response = new ApiResponse(paymentService.getResponseMessage(), payments);
        HttpStatus httpStatus;

        if (payments != null) httpStatus = HttpStatus.OK;
        else httpStatus = HttpStatus.BAD_REQUEST;

        return  ResponseEntity.status(httpStatus).body(response);
    }

    // Metode untuk memperbarui informasi payment dari fungsi yg telah dibuat di service
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updatePayment(@PathVariable("id") Long id, @RequestBody Payment payment) {
        Payment payments = paymentService.updatePayment(payment.getTotalPaid(), payment.getDiscount(), id);
        ApiResponse response = new ApiResponse(paymentService.getResponseMessage(), payments);
        HttpStatus httpStatus;

        if (payments != null)  httpStatus = HttpStatus.OK;
        else httpStatus = HttpStatus.BAD_REQUEST;

        return ResponseEntity.status(httpStatus).body(response);
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

