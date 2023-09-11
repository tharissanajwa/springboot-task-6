package com.springboot.task6.services;

import com.springboot.task6.model.TableOrder;
import com.springboot.task6.repositories.TableRepository;
import com.springboot.task6.utilities.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class TableService {
    @Autowired
    private TableRepository repository;

    private String responseMessage; // Pesan status untuk memberi informasi kepada pengguna

    // Metode untuk mendapatkan pesan status
    public String getResponseMessage() {
        return responseMessage;
    }

    // Metode untuk mendapatkan semua daftar meja yang belum terhapus melalui repository
    public List<TableOrder> getTableOrders() {
        if (repository.findAllByDeletedAtIsNull().isEmpty()) {
            seedData();
        }
        responseMessage = "Data successfully loaded.";
        return repository.findAllByDeletedAtIsNull();
    }

    // Metode untuk mendapatkan data meja berdasarkan id melalui repository
    public TableOrder getTableOrderById(Long id) {
        Optional<TableOrder> tableOrder = repository.findByIdAndDeletedAtIsNull(id);

        if (tableOrder.isPresent()) {
            responseMessage = "Data successfully loaded.";
            return tableOrder.get();
        } else {
            responseMessage = "Sorry, ID Table is not found.";
            return null;
        }
    }

    // Metode untuk menambahkan data meja baru melalui repository
    public TableOrder inserTableOrder(String name) {
        TableOrder newTableOrder = null;

        if (!Objects.equals(inputValidation(name), "")) {
            responseMessage = inputValidation(name);
        } else {
            newTableOrder = new TableOrder(Validation.inputTrim(name));
            repository.save(newTableOrder);
            responseMessage = "Data successfully added.";
        }
        return newTableOrder;
    }

    // Metode untuk memperbarui informasi meja melalui repository
    public TableOrder updateTableOrder(Long id, String name) {
        TableOrder tableOrder = getTableOrderById(id);

        if (tableOrder != null) {
            if (!inputValidation(name).equals("")) {
                responseMessage = inputValidation(name);
                return null;
            } else {
                tableOrder.setName(Validation.inputTrim(name));
                repository.save(tableOrder);
                responseMessage = "Data successfully updated!";
            }
        }

        return tableOrder;
    }

    // Metode untuk menghapus data meja secara soft delete melalui repository
    public boolean deleteTableOrder(Long id) {
        boolean result = false;
        TableOrder tableOrder = getTableOrderById(id);

        if (tableOrder != null) {
            tableOrder.setDeletedAt(new Date());
            repository.save(tableOrder);
            result = true;
            responseMessage = "Data successfully removed!";
        }
        return result;
    }

    // Metode untuk memvalidasi inputan pengguna
    private String inputValidation(String name) {
        String result = "";

        if (Validation.inputContainsNumber(Validation.inputTrim(name)) == 1) {
            result = "Sorry, table name cannot be blank.";
        } else if (Validation.inputContainsNumber(Validation.inputTrim(name)) == 2) {
            result = "Sorry, table name can only filled by letters and numbers";
        }

        return result;
    }

    // Metode untuk menambahkan sample awal
    public void seedData() {
        // database seeder
        TableOrder tableOrder1 = new TableOrder("A1");
        repository.save(tableOrder1);

        TableOrder tableOrder2 = new TableOrder("A2");
        repository.save(tableOrder2);

        TableOrder tableOrder3 = new TableOrder("A3");
        repository.save(tableOrder3);

        TableOrder tableOrder4 = new TableOrder("A4");
        repository.save(tableOrder4);

        TableOrder tableOrder5 = new TableOrder("A5");
        repository.save(tableOrder5);
    }
}
