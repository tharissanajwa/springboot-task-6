package com.springboot.task6.controllers;

import com.springboot.task6.model.ApiResponse;
import com.springboot.task6.model.Order;
import com.springboot.task6.model.OrderDetail;
import com.springboot.task6.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    // Metode untuk mengambil semua data order dari fungsi yg telah dibuat di service
    @GetMapping("")
    public ResponseEntity<ApiResponse> getAllOrder(@RequestParam boolean isPaid) {
        List<Order> orders;
        if (isPaid) {
            orders = orderService.getPaidOrders();
        } else {
            orders = orderService.getAllOrder();
        }

        ApiResponse response = new ApiResponse(orderService.getResponseMessage(), orders);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // Metode untuk mengambil data order berdasarkan id dari fungsi yg telah dibuat di service
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getOrderById(@PathVariable("id") Long id) {
        Order orders = orderService.getOrderById(id);
        ApiResponse response = new ApiResponse(orderService.getResponseMessage(), orders);
        HttpStatus httpStatus;

        if (orders != null) httpStatus = HttpStatus.OK;
        else httpStatus = HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(httpStatus).body(response);
    }

    // Metode untuk membuat order baru dari fungsi yg telah dibuat di service
    @PostMapping("")
    public ResponseEntity<ApiResponse> insertOrder(@RequestBody Order order) {
        Order orders = orderService.insertOrder(order.getEmployee().getId(), order.getTable().getId(), order.getMember().getId());
        ApiResponse response = new ApiResponse(orderService.getResponseMessage(), orders);
        HttpStatus httpStatus;

        if (orders != null) httpStatus = HttpStatus.OK;
        else httpStatus = HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(httpStatus).body(response);
    }

    // Metode untuk memperbarui informasi order dari fungsi yg telah dibuat di service
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse> updateOrder(@PathVariable("id") Long id, @RequestBody Order order) {
        Order orders = orderService.updateOrder(id, order.getNote());
        ApiResponse response = new ApiResponse(orderService.getResponseMessage(), orders);
        HttpStatus httpStatus;

        if (orders != null) httpStatus = HttpStatus.OK;
        else httpStatus = HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(httpStatus).body(response);
    }

    // Metode untuk menghapus order berdasarkan dari fungsi yg telah dibuat di service
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteOrder(@PathVariable("id") Long id) {
        boolean isValid = orderService.deleteOrder(id);
        ApiResponse response = new ApiResponse(orderService.getResponseMessage(), null);
        HttpStatus httpStatus;

        if (isValid) httpStatus = HttpStatus.OK;
        else httpStatus = HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(httpStatus).body(response);
    }

    // Metode untuk menambahkan product(detail order) kedalam order berdasarkan id yg diinputkan
    @PostMapping("/{id}/details")
    public ResponseEntity<ApiResponse> addProductToOrder(@PathVariable("id") Long orderId, @RequestBody OrderDetail detail) {
        OrderDetail orderDetail = orderService.addProductToOrder(orderId, detail.getProduct().getId(), detail.getQty());
        ApiResponse response = new ApiResponse(orderService.getResponseMessage(), orderDetail);
        HttpStatus httpStatus;

        if (orderDetail != null) httpStatus = HttpStatus.OK;
        else httpStatus = HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(httpStatus).body(response);
    }

    // Metode untuk mengubah status product(detail order) menjadi done berdasarkan id detail order yg diinputkan
    @PatchMapping("/{id}/details/{detailOrderId}")
    public ResponseEntity<ApiResponse> setProductDone(@PathVariable("id") Long id, @PathVariable("detailOrderId") Long detailOrderId) {
        OrderDetail orderDetail = orderService.setOrderDetailDone(id, detailOrderId);
        ApiResponse response = new ApiResponse(orderService.getResponseMessage(), orderDetail);
        HttpStatus httpStatus;

        if (orderDetail != null) httpStatus = HttpStatus.OK;
        else httpStatus = HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(httpStatus).body(response);
    }

    // Metode untuk menghapus product(detail order) berdasarkan id detail order yg diinputkan
    @DeleteMapping("/{id}/details/{detailOrderId}")
    public ResponseEntity<ApiResponse> deleteOrderDetail(@PathVariable("id") Long id, @PathVariable("detailOrderId") Long detailOrderId) {
        boolean orderDetail = orderService.deleteOrderDetail(id, detailOrderId);
        ApiResponse response = new ApiResponse(orderService.getResponseMessage(), null);
        HttpStatus httpStatus;

        if (orderDetail) httpStatus = HttpStatus.OK;
        else httpStatus = HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(httpStatus).body(response);
    }
}
