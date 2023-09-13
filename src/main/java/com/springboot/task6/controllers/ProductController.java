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
    public ResponseEntity<ApiResponse> getAllProduct() {
        List<Product> products = productService.getAllProduct();
        ApiResponse response = new ApiResponse(productService.getResponseMessage(), products);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // Metode untuk mengambil data barang berdasarkan id dari fungsi yg telah dibuat di service
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable("id") Long id) {
        Product products = productService.getProductById(id);
        ApiResponse response = new ApiResponse(productService.getResponseMessage(), products);
        HttpStatus httpStatus;

        if (products != null) httpStatus = HttpStatus.OK;
        else httpStatus = HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(httpStatus).body(response);
    }

    // Metode untuk membuat barang baru dari fungsi yg telah dibuat di service
    @PostMapping("")
    public ResponseEntity<ApiResponse> insertProduct(@RequestBody Product product) {
        Product newProduct = productService.insertProduct(product.getName(), product.getPrice(), product.getDescription());
        ApiResponse response = new ApiResponse(productService.getResponseMessage(), newProduct);
        HttpStatus httpStatus;

        if (newProduct != null) httpStatus = HttpStatus.OK;
        else httpStatus = HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(httpStatus).body(response);
    }

    // Metode untuk memperbarui informasi barang dari fungsi yg telah dibuat di service
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateProduct(@PathVariable("id") Long id, @RequestBody Product product) {
        Product products = productService.updateProduct(id, product.getName(), product.getPrice(), product.getDescription());
        ApiResponse response = new ApiResponse(productService.getResponseMessage(), products);
        HttpStatus httpStatus;

        if (products != null) httpStatus = HttpStatus.OK;
        else httpStatus = HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(httpStatus).body(response);
    }

    // Metode untuk menghapus barang berdasarkan dari fungsi yg telah dibuat di service
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable("id") Long id) {
        boolean isValid = productService.deleteProduct(id);
        ApiResponse response = new ApiResponse(productService.getResponseMessage(), null);
        HttpStatus httpStatus;

        if (isValid) httpStatus = HttpStatus.OK;
        else httpStatus = HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(httpStatus).body(response);
    }
}
