package com.springboot.task6.services;

import com.springboot.task6.model.Employee;
import com.springboot.task6.model.Member;
import com.springboot.task6.model.Order;
import com.springboot.task6.model.OrderDetail;
import com.springboot.task6.model.Product;
import com.springboot.task6.model.TableOrder;
import com.springboot.task6.repositories.OrderDetailRepository;
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
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private MemberService memberService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private TableService tableService;

    @Autowired
    private ProductService productService;

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
            responseMessage = "Data successfully loaded.";
        }
        return orders;
    }

    // Metode untuk mendapatkan data order berdasarkan id melalui repository
    public Order getOrderById(Long id) {
        Optional<Order> result = orderRepository.findByIdAndDeletedAtIsNull(id);

        if (result.isPresent()) {
            responseMessage = "Data successfully loaded.";
            return result.get();
        } else {
            responseMessage = "Sorry, id order is not found.";
            return null;
        }
    }

    // Metode untuk menambahkan order baru ke dalam data melalui repository
    public Order insertOrder(Long employeeId, Long tableId, Long memberId) {
        Order result = null;

        if (inputValidation(tableId, memberId, employeeId).isEmpty()) {
            Member member = memberService.getMemberById(memberId);
            Employee employee = employeeService.getEmployeeById(employeeId);
            TableOrder tableOrder = tableService.getTableOrderById(tableId);

            if (member == null) result = new Order(employee, tableOrder);
            else result = new Order(member, employee, tableOrder);

            tableService.getTableOrderById(tableId).setAvailable(false);
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

        if (!order.isPaid() && !order.getOrderDetails().isEmpty()) {
            order.setDeletedAt(new Date());
            orderRepository.save(order);
            result = true;
            responseMessage = "Data successfully removed.";
        } else {
            responseMessage = "Sorry, payment can't be made.";
        }

        return result;
    }

    // Metode untuk menambahkan product(detail order) kedalam order berdasarkan id yg diinputkan
    public OrderDetail addProductToOrder(Long orderId, Long productId, int qty) {
        OrderDetail result = null;
        Order order = getOrderById(orderId);
        Product product = productService.getProductById(productId);

        if (order != null) {
            if (product != null) {
                if (qty > 0) {
                    if (!order.isPaid()) {
                        result = new OrderDetail(order, product, qty);
                        order.addOrderDetail(result);
                        int totalAmount = accumulateTotalAmount(order);

                        order.setTotalAmount(totalAmount);
                        if (order.getMember() != null) {
                            int pointObtained = accumulatePoints(result);
                            order.setPointObtained(pointObtained);
                        }

                        orderDetailRepository.save(result);
                        orderRepository.save(order);
                    } else {
                        responseMessage = "Sorry, order with ID " + order.getId() + " has been paid.";
                    }
                } else {
                    responseMessage = "Sorry, quantity must be positive number.";
                }
            } else {
                responseMessage = "Sorry, product with ID " + productId + " doesn't exist.";
            }
        }

        return result;
    }

    // Metode untuk mengubah status product(detail order) menjadi done berdasarkan id detail order yg diinputkan
    public OrderDetail setOrderDetailDone(Long orderId, Long detailOrderId) {
        OrderDetail orderDetail = getOrderDetailById(orderId, detailOrderId);

        if (orderDetail != null) {
            orderDetail.setDone(true);
            responseMessage = "Product successfully set to done.";
            orderDetailRepository.save(orderDetail);
        }

        return orderDetail;
    }

    // Metode untuk menghapus product(detail order) berdasarkan id detail order yg diinputkan
    public boolean deleteOrderDetail(Long orderId, Long detailOrderId) {
        boolean result = true;
        OrderDetail orderDetail = getOrderDetailById(orderId, detailOrderId);

        if (orderDetail != null) {
            if (!orderDetail.isDone()) {
                orderDetail.setDeletedAt(new Date());
                orderDetailRepository.deleteById(detailOrderId);
                Order order = getOrderById(orderId);
                int total = accumulateTotalAmount(order);
                getOrderById(orderId).setTotalAmount(total);
                if (order.getMember() != null) {
                    int pointObtained = total/1000;
                    order.setPointObtained(pointObtained);
                }
                orderRepository.save(order);
                responseMessage = "Data order detail successfully removed.";
            } else {
                result = false;
                responseMessage = "Sorry, order with product ID " + detailOrderId + " has been done.";
            }
        }

        return result;
    }

    // Metode untuk memvalidasi inputan pengguna
    private String inputValidation(Long tableId, Long memberId, Long employeeId) {
        String result = "";

        if (tableService.getTableOrderById(tableId) != null && !tableService.getTableOrderById(tableId).isAvailable()) {
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

    // Metode untuk menghitung total pesanan yang harus dibayarkan
    private int accumulateTotalAmount(Order order) {
        int total = 0;

        for (int i = 0; i < order.getOrderDetails().size(); i++) { // looping through list<OrderDetail> in Order obj
            OrderDetail detail = order.getOrderDetails().get(i);
            int productPrice = detail.getProduct().getPrice();
            int qty = detail.getQty();
            total += (productPrice * qty); // add price * qty to total variable for every iteration
        }

        return total;
    }

    // Metode untuk menghitung total point yang didapatkan member
    private int accumulatePoints(OrderDetail orderDetail) {
        int pointFromOrder = orderDetail.getOrder().getPointObtained();
        int productTotalPrice = orderDetail.getProduct().getPrice() * orderDetail.getQty();
        int pointToAdd = productTotalPrice / 1000;

        pointFromOrder += pointToAdd; // accumulate existing point in Order obj with pointToAdd variable

        return pointFromOrder;
    }

    // Metode untuk mendapatkan detail order berdasarkan orderId dan detailOrderId
    private OrderDetail getOrderDetailById(Long orderId, Long detailOrderId) {
        OrderDetail orderDetail = null;
        List<OrderDetail> orderDetails = orderDetailRepository.getOrderDetailsByOrderIdAndDeletedAtIsNull(orderId);
        int i = 0;

        while (i < orderDetails.size()) {
            OrderDetail orderDetailFromLoop = orderDetails.get(i);
            Order order = orderDetailFromLoop.getOrder();

            if (order.getId().equals(orderId) && orderDetailFromLoop.getId().equals(detailOrderId)) {
                orderDetail = orderDetailFromLoop;
            }
            i++;
        }

        if (orderDetail == null) {
            responseMessage = "Sorry, order with ID " + orderId + " doesn't have any product yet.";
        }

        return orderDetail;
    }
}
