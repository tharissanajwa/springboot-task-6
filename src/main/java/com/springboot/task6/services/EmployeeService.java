package com.springboot.task6.services;

import com.springboot.task6.repositories.EmployeeRepository;
import com.springboot.task6.model.Employee;
import com.springboot.task6.utilities.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    private String responseMessage; // Pesan status untuk memberi informasi kepada pengguna

    // Metode untuk mendapatkan pesan status
    public String getResponseMessage() {
        return responseMessage;
    }

    // Metode untuk mendapatkan semua daftar pegawai yang belum terhapus melalui repository
    public List<Employee> getAllEmployee() {
        if (employeeRepository.findAllByDeletedAtIsNull().isEmpty()) {
            seedData();
        }
        responseMessage = "Data successfully loaded.";
        return employeeRepository.findAllByDeletedAtIsNull();
    }

    // Metode untuk mendapatkan data pegawai berdasarkan id melalui repository
    public Employee getEmployeeById(Long id) {
        Optional<Employee> result = employeeRepository.findByIdAndDeletedAtIsNull(id);
        if (result.isPresent()) {
            responseMessage = "Data successfully loaded.";
            return result.get();
        }
        responseMessage = "Sorry, id employee is not found.";
        return null;
    }

    // Metode untuk menambahkan pegawai baru ke dalam data melalui repository
    public Employee insertEmployee(String name) {
        Employee result = null;
        if (inputValidation(name) != "") {
            responseMessage = inputValidation(name);
        } else {
            result = new Employee(Validation.inputTrim(name));
            result.setCreatedAt(new Date());
            employeeRepository.save(result);
            responseMessage = "Data successfully added!";
        }
        return result;
    }

    // Metode untuk memperbarui informasi pegawai melalui repository
    public Employee updateEmployee(Long id, String name) {
        Employee result = getEmployeeById(id);
        if (result != null) {
            if (inputValidation(name) != "") {
                responseMessage = inputValidation(name);
                return null;
            } else {
                result.setName(Validation.inputTrim(name));
                result.setUpdatedAt(new Date());
                employeeRepository.save(result);
                responseMessage = "Data successfully updated!";
            }
        }

        return result;
    }

    // Metode untuk menghapus data pegawai secara soft delete melalui repository
    public boolean deleteEmployee(Long id) {
        boolean result = false;
        Employee employee = getEmployeeById(id);
        if (employee != null) {
            employee.setDeletedAt(new Date());
            employeeRepository.save(employee);
            result = true;
            responseMessage = "Data successfully removed.";
        }
        return result;
    }

    // Metode untuk memvalidasi inputan pengguna
    private String inputValidation(String name) {
        String result = "";
        if (Validation.inputCheck(Validation.inputTrim(name)) == 1) {
            result = "Sorry, employee name cannot be blank.";
        }
        if (Validation.inputCheck(Validation.inputTrim(name)) == 2) {
            result = "Sorry, employee name can only filled by letters";
        }
        return result;
    }

    // Metode untuk menambahkan sample awal
    private void seedData() {
        Employee employee1 = new Employee("Ahmad Budi Santoso");
        employeeRepository.save(employee1);

        Employee employee2 = new Employee("Maria Dewi Suryani");
        employeeRepository.save(employee2);

        Employee employee3 = new Employee("Rizky Pratama Putra");
        employeeRepository.save(employee3);

        Employee employee4 = new Employee("Siti Aisyah Rahman");
        employeeRepository.save(employee4);

        Employee employee5 = new Employee("Fajar Hidayatullah");
        employeeRepository.save(employee5);
    }
}
