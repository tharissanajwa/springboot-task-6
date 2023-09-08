package com.springboot.task6.controllers;

import com.springboot.task6.model.ApiResponse;
import com.springboot.task6.model.Product;
import com.springboot.task6.services.ProductService;
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
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    // Metode untuk mengambil semua data barang dari fungsi yg telah dibuat di service
    @GetMapping("")
    public ResponseEntity getAllProduct() {
        List<Product> employees = productService.getAllProduct();
        ApiResponse response = new ApiResponse(productService.getResponseMessage(), employees);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // Metode untuk mengambil data barang berdasarkan id dari fungsi yg telah dibuat di service
    @GetMapping("/{id}")
    public ResponseEntity getProductById(@PathVariable("id") Long id) {
        Product products = productService.getProductById(id);
        ApiResponse response = new ApiResponse(productService.getResponseMessage(), products);
        if (products != null) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    // Metode untuk membuat product baru dari fungsi yg telah dibuat di service
    @PostMapping("")
    public ResponseEntity insertProduct(@RequestBody Product product) {
        Product newProduct = productService.insertProduct(product.getName(), product.getPrice());
        ApiResponse response = new ApiResponse(productService.getResponseMessage(), newProduct);
        if (newProduct != null) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    // Metode untuk memperbarui informasi pegawai dari fungsi yg telah dibuat di service
    @PutMapping("/{id}")
    public ResponseEntity updateProduct(@PathVariable("id") Long id, @RequestBody Product product) {
        Product products = productService.updateProduct(id, product.getName(), product.getPrice());
        ApiResponse response = new ApiResponse(productService.getResponseMessage(), products);
        if (products != null) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    // Metode untuk menghapus pegawai berdasarkan dari fungsi yg telah dibuat di service
    @DeleteMapping("/{id}")
    public ResponseEntity deleteProduct(@PathVariable("id") Long id) {
        boolean valid = productService.deleteProduct(id);
        ApiResponse response = new ApiResponse(productService.getResponseMessage(), null);
        if (valid) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
