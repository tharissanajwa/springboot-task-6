package com.springboot.task6.seeders;

import com.springboot.task6.model.TableOrder;
import com.springboot.task6.repositories.TableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class TableSeeder {
    @Autowired
    private TableRepository tableRepository;

    @PostConstruct
    public void seed() {
        // Daftar pesanan meja yang akan disimpan dalam database
        List<TableOrder> tableOrders = new ArrayList<>(Arrays.asList(
                new TableOrder("A1"),
                new TableOrder("A2"),
                new TableOrder("A3"),
                new TableOrder("A4"),
                new TableOrder("A5")
        ));

        // Cek apakah database sudah berisi data pesanan meja atau tidak
        if (tableRepository.findAllByDeletedAtIsNullOrderByName().isEmpty()) {
            // Jika tidak ada data, maka simpan data pesanan meja ke dalam database
            tableRepository.saveAll(tableOrders);
        }
    }
}