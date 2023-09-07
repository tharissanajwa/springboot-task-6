package com.springboot.task6.model;

import javax.persistence.*;

@Entity
@Table(name = "tables")
public class TableOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer name;
}
