package com.springboot.task6.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "products")
public class Product {
    @Id
    private Long id;
    private String name;
    private Integer price;
}
