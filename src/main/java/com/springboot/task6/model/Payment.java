package com.springboot.task6.model;

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

    public Payment(Integer totalPaid, Integer discount, Integer change) {
        this.totalPaid = totalPaid;
        this.discount = discount;
        this.change = change;
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

    public Date getCreatedAt() {
        return this.createdAt;
    }

    public Date getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Date getDeletedAt() {
        return this.deletedAt;
    }

    public void setDeletedAt(Date deletedAt) {
        this.deletedAt = deletedAt;
    }
}
