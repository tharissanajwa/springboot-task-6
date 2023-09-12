package com.springboot.task6.services;

import com.springboot.task6.model.Employee;
import com.springboot.task6.model.Product;
import com.springboot.task6.repositories.ProductRepository;
import com.springboot.task6.utilities.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    // Pesan status untuk memberi informasi kepada pengguna
    private String responseMessage;

    // Metode untuk mendapatkan pesan status
    public String getResponseMessage() {
        return responseMessage;
    }

    // Metode untuk mendapatkan semua daftar barang yang belum terhapus melalui repository
    public List<Product> getAllProduct() {
        if (productRepository.findAllByDeletedAtIsNull().isEmpty()) {
            seedData();
        }
        responseMessage = "Data successfully loaded.";
        return productRepository.findAllByDeletedAtIsNull();
    }

    // Metode untuk mendapatkan data barang berdasarkan id melalui repository
    public Product getProductById(Long id) {
        Optional<Product> result = productRepository.findByIdAndDeletedAtIsNull(id);
        if (!result.isPresent()) {
            responseMessage = "Sorry, id product is not found.";
            return null;
        }
        responseMessage = "Data successfully loaded.";
        return result.get();

    }

    // Metode untuk menambahkan barang ke dalam data melalui repository
    public Product insertProduct(String name, Integer price) {
        Product result = null;
        String nameValidation = inputValidation(name);
        String priceValidation = inputValidationPrice(price);

        if (!nameValidation.isEmpty()) {
            responseMessage = nameValidation;
        } else if (!priceValidation.isEmpty()) {
            responseMessage = priceValidation;
        } else {
            result = new Product(Validation.inputTrim(name), price);
            result.setCreatedAt(new Date());
            productRepository.save(result);
            responseMessage = "Data successfully added!";
        }
        return result;
    }

    // Metode untuk memperbarui informasi barang melalui repository
    public Product updateProduct(Long id, String name, Integer price) {
        Product result = getProductById(id);
        String nameValidation = inputValidation(name);
        String priceValidation = inputValidationPrice(price);

        if (result != null) {
            if (!nameValidation.isEmpty()) {
                responseMessage = nameValidation;
                return null;
            } else if (!priceValidation.isEmpty()) {
                responseMessage = priceValidation;
                return null;
            } else {
                result.setName(Validation.inputTrim(name));
                result.setPrice(price);
                result.setUpdatedAt(new Date());
                productRepository.save(result);
                responseMessage = "Data successfully updated!";
            }
        }
        return result;
    }

    // Metode untuk menghapus data barang secara soft delete melalui repository
    public boolean deleteProduct(Long id) {
        boolean result = false;
        Product product = getProductById(id);
        if (getProductById(id) != null) {
            getProductById(id).setDeletedAt(new Date());
            productRepository.save(product);
            result = true;
            responseMessage = "Data successfully removed!";
        }
        return result;
    }

    // Metode untuk memvalidasi inputan pengguna
    private String inputValidation(String name) {
        String result = "";
        if (Validation.inputCheck(Validation.inputTrim(name)) == 1) {
            result = "Sorry, product name cannot be blank.";
        }
        if (Validation.inputCheck(Validation.inputTrim(name)) == 2) {
            result = "Sorry, product name can only filled by letters";
        }
        return result;
    }

    // Metode untuk memvalidasi inputan pengguna untuk harga (price)
    private String inputValidationPrice(Integer price) {
        String result = "";
        if (price <= 0) {
            result = "Sorry, price must be more than 0.";
        }
        return result;
    }

    // Metode untuk menambahkan sample awal
    private void seedData() {
        Product product1 = new Product("Nasi Timbel Sunda", 40_000);
        productRepository.save(product1);

        Product product2 = new Product("Sate Maranggi", 30_000);
        productRepository.save(product2);

        Product product3 = new Product("Pepes Ikan Mujair", 45_000);
        productRepository.save(product3);

        Product product4 = new Product("Terong Bacem", 20_000);
        productRepository.save(product4);

        Product product5 = new Product("Karedok", 25_000);
        productRepository.save(product5);
    }
}
