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
        List<Product> products = new ArrayList<>(Arrays.asList(
                new Product("Nasi Timbel Sunda", 40_000),
                new Product("Sate Maranggi", 30_000),
                new Product("Pepes Ikan Mujair", 45_000),
                new Product("Terong Bacem", 20_000),
                new Product("Karedok", 25_000)
        ));

        if (productRepository.findAllByDeletedAtIsNull().isEmpty()) {
            productRepository.saveAll(products);
        }
    }
}
