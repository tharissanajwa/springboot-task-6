package com.springboot.task6.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    private Member member;
    @ManyToOne
    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    private Employee employee;
    @ManyToOne
    @JoinColumn(name = "table_id", referencedColumnName = "id")
    private TableOrder table;
    @OneToMany(mappedBy = "order")
    private List<OrderDetail> orderDetails;
    @Column(name = "total_amount")
    private Integer totalAmount;
    private String note;
    @Column(name = "is_paid")
    private Boolean isPaid;
    @Column(name = "point_obtained")
    private Integer pointObtained; // Poin yang akan didapatkan member
    @OneToOne(mappedBy = "order")
    private Payment payment;
    @Column(name = "created_at", columnDefinition = "DATE DEFAULT CURRENT_DATE")
    private Date createdAt;
    @Column(name = "updated_at", columnDefinition = "DATE DEFAULT NULL")
    private Date updatedAt;
    @Column(name = "deleted_at", columnDefinition = "DATE DEFAULT NULL")
    private Date deletedAt;

    public Order() {
    }

    public Order(Member member, Employee employee, TableOrder table) {
        this.member = member;
        this.employee = employee;
        this.table = table;
        this.orderDetails = new ArrayList<>();
        this.note = "";
        this.pointObtained = 0;
        this.totalAmount = 0;
        this.isPaid = false;
    }

    public Order(Employee employee, TableOrder table) {
        this.member = null;
        this.employee = employee;
        this.table = table;
        this.orderDetails = new ArrayList<>();
        this.note = "";
        this.pointObtained = 0;
        this.totalAmount = 0;
        this.isPaid = false;
    }

    public Long getId() {
        return this.id;
    }

    public int getTotalAmount() {
        return this.totalAmount;
    }

    public String getNote() {
        return this.note;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public TableOrder getTable() {
        return table;
    }

    public void setTable(TableOrder table) {
        this.table = table;
    }

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setIsPaid(boolean isPaid) {
        this.isPaid = isPaid;
    }

    public int getPointObtained() {
        return pointObtained;
    }

    public void setPointObtained(int pointObtained) {
        this.pointObtained = pointObtained;
    }

    public void addOrderDetail(OrderDetail detail) {
        this.orderDetails.add(detail);
    }

    public void removeOrderDetail(OrderDetail detail) {
        this.orderDetails.remove(detail);
    }

    public void addTotalAmount(int amount) {
        this.totalAmount += amount;
    }

    public Date getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
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
