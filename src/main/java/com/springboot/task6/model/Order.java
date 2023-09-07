package com.springboot.task6.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "total_amount")
    private Integer totalAmount;
    private String note;

    @ManyToOne
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "table_id", referencedColumnName = "id")
    private TableOrder table;

//    @JsonIgnore
//    @OneToMany(mappedBy = "order")
//    private List<OrderProduct> orderProducts;
    private String notes;
    @Column(name = " is_paid", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isPaid;
    @Column(name = "created_at", columnDefinition = "DATE DEFAULT CURRENT_DATE")
    private Date createdAt;
    @Column(name = "updated_at", columnDefinition = "DATE DEFAULT NULL")
    private Date updatedAt;
    @Column(name = "deleted_at", columnDefinition = "DATE DEFAULT NULL")
    private Date deletedAt;
    @OneToOne(mappedBy = "order")
    private Payment payment;

    public Order(Integer totalAmount, String note, Boolean isPaid) {
        this.totalAmount = totalAmount;
        this.note = note;
        this.isPaid = isPaid;
        this.payment = null;
    }

    public Long getId() {
        return this.id;
    }

    public Integer getTotalAmount() {
        return this.totalAmount;
    }

    public String getNote() {
        return this.note;
    }

    public Boolean getIsPaid() {
        return this.isPaid;
    }

    public void setIsPaid(Boolean isPaid) {
        this.isPaid = isPaid;
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
