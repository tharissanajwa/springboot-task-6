package com.springboot.task6.services;

import com.springboot.task6.model.Product;
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

    private String responseMessage;

    public String getResponseMessage() {
        return responseMessage;
    }

    public List<TableOrder> getTableOrders() {
        List<TableOrder> result = repository.findAllByDeletedAtIsNull();

        if (result.isEmpty()) {
            responseMessage = "Data successfully loaded.";
            seedData();
        } else {
            responseMessage = "Data successfully loaded.";
        }

        return result;
    }

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

    private String inputValidation(String name) {
        String result = "";

        if (Validation.inputContainsNumber(Validation.inputTrim(name)) == 1) {
            result = "Sorry, table name cannot be blank.";
        } else if (Validation.inputContainsNumber(Validation.inputTrim(name)) == 2) {
            result = "Sorry, table name can only filled by letters and numbers";
        }

        return result;
    }

    public void seedData() {
        // database seeder
        TableOrder tableOrder1 = new TableOrder();
        tableOrder1.setName("A1");
        tableOrder1.setCreatedAt(new Date());
        repository.save(tableOrder1);

        TableOrder tableOrder2 = new TableOrder();
        tableOrder2.setName("A2");
        tableOrder2.setCreatedAt(new Date());
        repository.save(tableOrder2);

        TableOrder tableOrder3 = new TableOrder();
        tableOrder3.setName("A3");
        tableOrder3.setCreatedAt(new Date());
        repository.save(tableOrder3);

        TableOrder tableOrder4 = new TableOrder();
        tableOrder4.setName("A4");
        tableOrder4.setCreatedAt(new Date());
        repository.save(tableOrder4);

        TableOrder tableOrder5 = new TableOrder();
        tableOrder5.setName("A5");
        tableOrder5.setCreatedAt(new Date());
        repository.save(tableOrder5);
    }
}
