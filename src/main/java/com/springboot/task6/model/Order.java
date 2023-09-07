package com.springboot.task6.model;

import javax.persistence.*;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer totalAmount;
    private String note;
    private Boolean isPaid;
    private Date createdAt;
    private Date updatedAt;
    private Date deletedAt;
}
