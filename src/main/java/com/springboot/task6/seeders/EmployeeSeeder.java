package com.springboot.task6.seeders;

import com.springboot.task6.model.Employee;
import com.springboot.task6.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class EmployeeSeeder {
    @Autowired
    private EmployeeRepository employeeRepository;

    @PostConstruct
    public void seed() {
        List<Employee> employees = new ArrayList<>(Arrays.asList(
                new Employee("Ahmad Budi Santoso"),
                new Employee("Maria Dewi Suryani"),
                new Employee("Rizky Pratama Putra"),
                new Employee("Siti Aisyah Rahman"),
                new Employee("Fajar Hidayatullah")
        ));

        if (employeeRepository.findAllByDeletedAtIsNullOrderByName().isEmpty()) {
            employeeRepository.saveAll(employees);
        }
    }
}
