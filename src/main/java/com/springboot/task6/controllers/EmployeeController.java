package com.springboot.task6.controllers;

import com.springboot.task6.model.ApiResponse;
import com.springboot.task6.model.Employee;
import com.springboot.task6.services.EmployeeService;
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
@RequestMapping("/employees")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    // Metode untuk mengambil semua data pegawai dari fungsi yg telah dibuat di service
    @GetMapping
    public ResponseEntity<ApiResponse> getAllEmployee() {
        List<Employee> employees = employeeService.getAllEmployee();
        ApiResponse response = new ApiResponse(employeeService.getResponseMessage(), employees);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // Metode untuk mengambil data pegawai berdasarkan id dari fungsi yg telah dibuat di service
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getEmployeeById(@PathVariable("id") Long id) {
        Employee employees = employeeService.getEmployeeById(id);
        ApiResponse response = new ApiResponse(employeeService.getResponseMessage(), employees);
        HttpStatus httpStatus;

        if (employees != null) httpStatus = HttpStatus.OK;
        else httpStatus = HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(httpStatus).body(response);
    }

    // Metode untuk membuat pegawai baru dari fungsi yg telah dibuat di service
    @PostMapping
    public ResponseEntity<ApiResponse> insertEmployee(@RequestBody Employee employee) {
        Employee employees = employeeService.insertEmployee(employee.getName(), employee.getPhone());
        ApiResponse response = new ApiResponse(employeeService.getResponseMessage(), employees);
        HttpStatus httpStatus;

        if (employees != null) httpStatus = HttpStatus.OK;
        else httpStatus = HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(httpStatus).body(response);
    }

    // Metode untuk memperbarui informasi pegawai dari fungsi yg telah dibuat di service
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateEmployee(@PathVariable("id") Long id, @RequestBody Employee employee) {
        Employee employees = employeeService.updateEmployee(id, employee.getName(), employee.getPhone());
        ApiResponse response = new ApiResponse(employeeService.getResponseMessage(), employees);
        HttpStatus httpStatus;

        if (employees != null) httpStatus = HttpStatus.OK;
        else httpStatus = HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(httpStatus).body(response);
    }

    // Metode untuk menghapus pegawai berdasarkan dari fungsi yg telah dibuat di service
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteEmployee(@PathVariable("id") Long id) {
        boolean isValid = employeeService.deleteEmployee(id);
        ApiResponse response = new ApiResponse(employeeService.getResponseMessage(), null);
        HttpStatus httpStatus;

        if (isValid) httpStatus = HttpStatus.OK;
        else httpStatus = HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(httpStatus).body(response);
    }
}
