package com.springboot.task6.services;

import com.springboot.task6.model.Order;
import com.springboot.task6.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MemberService memberService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private TableService tableService;

    @Autowired
    private OrderDetailService orderDetailService;

    private String responseMessage; // Pesan status untuk memberi informasi kepada pengguna

    // Metode untuk mendapatkan pesan status
    public String getResponseMessage() {
        return responseMessage;
    }

    // Metode untuk mendapatkan semua daftar order yang belum terhapus melalui repository
    public List<Order> getAllOrder() {
        if (orderRepository.findAllByDeletedAtIsNull().isEmpty()) {
            responseMessage = "Data doesn't exists, please insert new data order.";
        } else {
            responseMessage = "Data successfully loaded.";
        }
        return orderRepository.findAllByDeletedAtIsNull();
    }

    // Metode untuk mendapatkan data order berdasarkan id melalui repository
    public Order getOrderById(Long id) {
        Optional<Order> result = orderRepository.findByIdAndDeletedAtIsNull(id);
        if (result.isPresent()) {
            responseMessage = "Data successfully loaded.";
            return result.get();
        }
        responseMessage = "Sorry, id order is not found.";
        return null;
    }

    // Metode untuk menambahkan order baru ke dalam data melalui repository
    public Order insertOrder(Long employeeId, Long tableId, Long memberId) {
        Order result = null;
        result = new Order(memberService.getMemberById(memberId), employeeService.getEmployeeById(employeeId), tableService.getTableOrderById(tableId));
        result.setCreatedAt(new Date());
        orderRepository.save(result);
        responseMessage = "Data successfully added!";
        return result;
    }

    // Metode untuk memperbarui informasi order melalui repository
    public Order updateOrder(Long id, String note) {
        Order result = getOrderById(id);
        if (result != null) {
            Integer total = 0;
            for (int indeks = 0; indeks <= orderDetailService.getOrderDetailByOrderId(id).size()-1; indeks++) {
                total += (orderDetailService.getOrderDetailByOrderId(id).get(indeks).getProduct().getPrice() * orderDetailService.getOrderDetailByOrderId(id).get(indeks).getQuantity());
            }
            result.setOrderDetails(orderDetailService.getOrderDetailByOrderId(id));
            result.setTotalAmount(total);
            result.setNote(note);
            result.setUpdatedAt(new Date());
            orderRepository.save(result);
            responseMessage = "Data successfully updated!";
        }
        return result;
    }

    // Metode untuk menghapus data order secara soft delete melalui repository
    public boolean deleteOrder(Long id) {
        boolean result = false;
        Order order = getOrderById(id);
        if (order != null) {
            order.setDeletedAt(new Date());
            orderRepository.save(order);
            result = true;
            responseMessage = "Data successfully removed.";
        }
        return result;
    }
}
