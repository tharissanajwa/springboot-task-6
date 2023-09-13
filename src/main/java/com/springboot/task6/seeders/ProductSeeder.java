package com.springboot.task6.seeders;

import com.springboot.task6.model.Product;
import com.springboot.task6.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class ProductSeeder {

    @Autowired
    private ProductRepository productRepository;

    @PostConstruct
    public void seed() {
        // Daftar produk yang akan disimpan dalam database
        List<Product> products = new ArrayList<>(Arrays.asList(
                new Product("Nasi Timbel Sunda", 40_000, "Nasi beraroma daun pisang dengan aneka lauk tradisional."),
                new Product("Sate Maranggi", 30_000, "Sate sapi berbumbu khas Sunda dengan saus kacang."),
                new Product("Pepes Ikan Mujair", 45_000, "Ikan mujair dibungkus dalam daun pisang dan dimasak dengan rempah khas Sunda."),
                new Product("Terong Bacem", 20_000, "Terong digoreng dengan kuah manis berbumbu."),
                new Product("Karedok", 25_000, "Salad sayuran segar dengan saus kacang pedas khas Sunda.")
        ));

        // Cek apakah database sudah berisi data produk atau tidak
        if (productRepository.findAllByDeletedAtIsNullOrderByName().isEmpty()) {
            // Jika tidak ada data, maka simpan data produk ke dalam database
            productRepository.saveAll(products);
        }
    }
}