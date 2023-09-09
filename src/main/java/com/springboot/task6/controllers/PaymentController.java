package com.springboot.task6.controllers;

import com.springboot.task6.model.ApiResponse;
import com.springboot.task6.model.Payment;
import com.springboot.task6.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    // Metode untuk membuat pembayaran baru dari fungsi yg telah dibuat di service
    @PostMapping("/{order}")
    public ResponseEntity<ApiResponse> insertPayment(@PathVariable("order") Long order, @RequestBody Payment payment) {
        Payment payments = paymentService.insertPayment(payment.getTotalPaid(), payment.getDiscount(), order);
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