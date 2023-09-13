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
        // Daftar karyawan yang akan disimpan dalam database
        List<Employee> employees = new ArrayList<>(Arrays.asList(
                new Employee("Ahmad Budi Santoso", "081234567890"),
                new Employee("Maria Dewi Suryani", "085612345678"),
                new Employee("Rizky Pratama Putra", "081398765432"),
                new Employee("Siti Aisyah Rahman", "082255555555"),
                new Employee("Fajar Hidayatullah", "087788889999")
        ));

        // Cek apakah database sudah berisi data karyawan atau tidak
        if (employeeRepository.findAllByDeletedAtIsNullOrderByName().isEmpty()) {
            // Jika tidak ada data, maka simpan data karyawan ke dalam database
            employeeRepository.saveAll(employees);
        }
    }
}