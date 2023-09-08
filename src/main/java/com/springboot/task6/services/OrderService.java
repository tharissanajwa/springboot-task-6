package com.springboot.task6.services;

import com.springboot.task6.model.Order;
import com.springboot.task6.model.OrderDetail;
import com.springboot.task6.model.Product;
import com.springboot.task6.repositories.OrderDetailRepository;
import com.springboot.task6.repositories.OrderRepository;
import com.springboot.task6.utilities.Validation;
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
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private MemberService memberService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private TableService tableService;

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderDetailService orderDetailService;

    private String responseMessage; // Pesan status untuk memberi informasi kepada pengguna

    // Metode untuk mendapatkan pesan status
    public String getResponseMessage() {
        return responseMessage;
    }

    // Metode untuk mendapatkan semua daftar order yang belum terhapus melalui repository
    public List<Order> getAllOrder() {
        List<Order> orders = orderRepository.findAllByDeletedAtIsNull();

        if (orders.isEmpty()) {
            responseMessage = "Data doesn't exists, please insert new data order.";
        } else {
            for (int i = 0; i < orders.size() - 1;i++) {
                int total = 0;
                Order selectedOrder = orders.get(i);
                for (int indeks = 0; indeks <= selectedOrder.getOrderDetails().size()-1; indeks++) {
                    total += (selectedOrder.getOrderDetails().get(indeks).getProduct().getPrice() *selectedOrder.getOrderDetails().get(indeks).getQty());
                }
                selectedOrder.setTotalAmount(total);
                orderRepository.save(selectedOrder);
            }
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
        if (inputValidation(tableId, memberId, employeeId).isEmpty()) {
            result = new Order(memberService.getMemberById(memberId), employeeService.getEmployeeById(employeeId), tableService.getTableOrderById(tableId));
//            tableService.getTableOrderById(tableId).setAvailable(false);
            result.setCreatedAt(new Date());
            orderRepository.save(result);
            responseMessage = "Data successfully added!";
        } else {
            responseMessage = inputValidation(tableId, memberId, employeeId);
        }

        return result;
    }

    // Metode untuk memperbarui informasi order melalui repository
    public Order updateOrder(Long id, String note) {
        Order result = getOrderById(id);
        if (result != null) {
            Integer total = 0;
            for (int indeks = 0; indeks <= getOrderDetailByOrderId(id).size()-1; indeks++) {
                total += (getOrderDetailByOrderId(id).get(indeks).getProduct().getPrice() * getOrderDetailByOrderId(id).get(indeks).getQty());
            }
            result.setTotalAmount(total);
            result.setOrderDetails(getOrderDetailByOrderId(id));
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

    public OrderDetail createOrderDetail(Long orderId, Long productId, int qty) {
        OrderDetail result = null;
        Order order = getOrderById(orderId);
        Product product = productService.getProductById(productId);

        if (qty > 0) {
            if (!order.getIsPaid()) {
                result = new OrderDetail(order, product, qty);

                orderRepository.save(order);
                orderDetailRepository.save(result);
            } else {
                responseMessage = "Sorry, order with ID " + order.getId() + " has been paid.";
            }
        } else {
            responseMessage = "Sorry, item quantity must be positive number.";
        }

        return result;
    }

    // Metode untuk memvalidasi inputan pengguna
    private String inputValidation(Long tableId, Long memberId, Long employeeId) {
        String result = "";

        if (tableService.getTableOrderById(tableId) != null &&!tableService.getTableOrderById(tableId).isAvailable()) {
                result = "Sorry, this table is currently used by another customer";
        }
        if (tableService.getTableOrderById(tableId) == null) {
            result = "Sorry, the table is not found";
        }

        if (memberId != null && memberService.getMemberById(memberId) == null) {
            result = "Sorry, the member is not found";
        }

        if (employeeId == null || employeeService.getEmployeeById(employeeId) == null) {
            result = "Sorry, the employee is not found";
        }

        return result;
    }

    public List<OrderDetail> getOrderDetailByOrderId(Long orderId) {
        return orderDetailRepository.getOrderDetailsByOrderIdAndDeletedAtIsNull(orderId);
    }
}
