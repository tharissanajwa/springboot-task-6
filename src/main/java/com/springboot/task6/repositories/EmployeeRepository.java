package com.springboot.task6.repositories;

import com.springboot.task6.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findAllByDeletedAtIsNull();
    Optional<Employee> findByIdAndDeletedAtIsNull(Long id);
}
