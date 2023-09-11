package com.springboot.task6.repositories;

import com.springboot.task6.model.TableOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TableRepository extends JpaRepository<TableOrder, Long> {
    List<TableOrder> findAllByDeletedAtIsNull();
    Optional<TableOrder> findByIdAndDeletedAtIsNull(Long id);
}
