package com.springboot.task6.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.OneToOne;
import javax.persistence.JoinColumn;
import java.util.Date;

@Entity
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "total_paid")
    private Integer totalPaid;
    private Integer discount;
    private Integer change;
    @Column(name = "created_at", columnDefinition = "DATE DEFAULT CURRENT_DATE")
    private Date createdAt;
    @Column(name = "updated_at", columnDefinition = "DATE DEFAULT NULL")
    private Date updatedAt;
    @Column(name = "deleted_at", columnDefinition = "DATE DEFAULT NULL")
    private Date deletedAt;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;

    public Payment() {
    }

    public Payment(Integer totalPaid, Integer discount, Order order) {
        this.totalPaid = totalPaid;
        this.discount = discount;
        this.order = order;
    }

    public Long getId() {
        return this.id;
    }

    public Integer getTotalPaid() {
        return this.totalPaid;
    }

    public Integer getDiscount() {
        return this.discount;
    }

    public Integer getChange() {
        return this.change;
    }

    @JsonIgnore
    public Date getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @JsonIgnore
    public Date getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @JsonIgnore
    public Date getDeletedAt() {
        return this.deletedAt;
    }

    public void setDeletedAt(Date deletedAt) {
        this.deletedAt = deletedAt;
    }

    @JsonIgnore
    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public void setTotalPaid(Integer totalPaid) {
        this.totalPaid = totalPaid;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public void setChange(Integer change) {
        this.change = change;
    }
}
