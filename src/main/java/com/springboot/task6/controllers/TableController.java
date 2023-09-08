package com.springboot.task6.controllers;

import com.springboot.task6.model.ApiResponse;
import com.springboot.task6.model.TableOrder;
import com.springboot.task6.services.TableService;
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
@RequestMapping("/tables")
public class TableController {
    @Autowired
    private TableService service;

    @GetMapping("")
    public ResponseEntity<ApiResponse> getAllTable() {
        List<TableOrder> tables = service.getTableOrders();
        ApiResponse response = new ApiResponse(service.getResponseMessage(), tables);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getTableById(@PathVariable("id") Long id) {
        TableOrder tableOrder = service.getTableOrderById(id);
        ApiResponse response = new ApiResponse(service.getResponseMessage(), tableOrder);
        HttpStatus httpStatus;

        if (tableOrder != null) httpStatus = HttpStatus.OK;
        else httpStatus = HttpStatus.BAD_REQUEST;

        return ResponseEntity.status(httpStatus).body(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse> insertTableOrder(@RequestBody TableOrder tableOrder) {
        TableOrder table = service.inserTableOrder(tableOrder.getName());
        ApiResponse response = new ApiResponse(service.getResponseMessage(), table);
        HttpStatus httpStatus;

        if (table != null) httpStatus = HttpStatus.OK;
        else httpStatus = HttpStatus.BAD_REQUEST;

        return ResponseEntity.status(httpStatus).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateTableOrder(@PathVariable("id") Long id, @RequestBody TableOrder tableOrder) {
        TableOrder table = service.updateTableOrder(id, tableOrder.getName());
        ApiResponse response = new ApiResponse(service.getResponseMessage(), table);
        HttpStatus httpStatus;

        if (table != null) httpStatus = HttpStatus.OK;
        else httpStatus = HttpStatus.BAD_REQUEST;

        return ResponseEntity.status(httpStatus).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteTableOrder(@PathVariable("id") Long id) {
        boolean isValid = service.deleteTableOrder(id);
        ApiResponse response = new ApiResponse(service.getResponseMessage(), null);
        HttpStatus httpStatus;

        if (isValid) httpStatus = HttpStatus.OK;
        else httpStatus = HttpStatus.BAD_REQUEST;

        return ResponseEntity.status(httpStatus).body(response);
    }
}
