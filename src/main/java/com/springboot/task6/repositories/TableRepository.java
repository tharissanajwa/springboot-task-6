package com.springboot.task6.repositories;

import com.springboot.task6.model.TableOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TableRepository extends JpaRepository<TableOrder, Long> {
    List<TableOrder> findAllByDeletedAtIsNullOrderByName();
    Optional<TableOrder> findByIdAndDeletedAtIsNull(Long id);
    Optional<TableOrder> findByNameAndDeletedAtIsNull(String name);
}
