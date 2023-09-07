package com.springboot.task6.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "products")
public class Product {
    @Id
    private Long id;
    private String name;
    private Integer price;

//    @JsonIgnore
//    @OneToMany(mappedBy = "product")
//    private List<OrderProduct> orderProducts;

    @Column(name = "created_at", columnDefinition = "DATE DEFAULT CURRENT_DATE")
    private Date createdAt;
    @Column(name = "updated_at", columnDefinition = "DATE DEFAULT NULL")
    private Date updatedAt;
    @Column(name = "deleted_at", columnDefinition = "DATE DEFAULT NULL")
    private Date deletedAt;}
