package com.springboot.task6.services;

import com.springboot.task6.model.Employee;
import com.springboot.task6.model.Member;
import com.springboot.task6.model.Order;
import com.springboot.task6.model.OrderDetail;
import com.springboot.task6.model.Product;
import com.springboot.task6.model.TableOrder;
import com.springboot.task6.repositories.OrderDetailRepository;
import com.springboot.task6.repositories.OrderRepository;
import com.springboot.task6.repositories.TableRepository;
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
    private TableRepository tableRepository;

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
        List<Order> orders = orderRepository.findAllByDeletedAtIsNullOrderByIdDesc();
        if (orders.isEmpty()) {
            responseMessage = "Data doesn't exists, please insert new data order.";
        } else {
            responseMessage = "Data successfully loaded.";
        }
        return orders;
    }

    // Metode untuk mendapatkan semua daftar order yang belum terhapus dan sudah dibayar (rekap pencatatan transaksi) melalui repository
    public List<Order> getPaidOrders() {
        List<Order> orders = orderRepository.findAllByDeletedAtIsNullAndIsPaidTrueOrderByIdDesc();
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
        }
        responseMessage = "Sorry, id order is not found.";
        return null;
    }

    // Metode untuk menambahkan order baru ke dalam data melalui repository
    public Order insertOrder(Long employeeId, Long tableId, Long memberId) {
        Order result = null;

        if (inputValidation(tableId, memberId, employeeId).isEmpty()) {
            Member member = memberService.getMemberById(memberId);
            Employee employee = employeeService.getEmployeeById(employeeId);
            TableOrder tableOrder = tableService.getTableOrderById(tableId);

            if (member == null) result = new Order(employee, tableOrder); // create order obj with no member
            else result = new Order(member, employee, tableOrder); // create order obj with member assigned

            tableOrder.setAvailable(false); // set selected table status to not available
            result.setCreatedAt(new Date());

            tableRepository.save(tableOrder);
            orderRepository.save(result); // save order data to database
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

        if (validateDeleteOrder(order).isEmpty()) {
            order.setDeletedAt(new Date());
            orderRepository.save(order);
            result = true;
            responseMessage = "order successfully deleted.";
        } else {
            responseMessage = validateDeleteOrder(order);
        }

        return result;
    }

    // Metode untuk menambahkan product(detail order) kedalam order berdasarkan id yg diinputkan
    public OrderDetail addProductToOrder(Long orderId, Long productId, int qty) {
        OrderDetail result = null;
        Order order = getOrderById(orderId);
        Product product = productService.getProductById(productId);

        if (validateOrderDetailData(orderId, productId, qty).isEmpty()) {
            int totalPrice = product.getPrice() * qty;
            result = new OrderDetail(order, product, qty, totalPrice);
            order.addOrderDetail(result); // add result obj to order obj
            int totalAmount = accumulateTotalAmount(order);

            order.setTotalAmount(totalAmount);

            if (order.getMember() != null) {
                int pointObtained = accumulatePoints(order);
                order.setPointObtained(pointObtained);
            }

            orderDetailRepository.save(result);
            orderRepository.save(order);
        } else {
            responseMessage = validateOrderDetailData(orderId, productId, qty);
        }

        return result;
    }

    // Metode untuk mengubah status product(detail order) menjadi done berdasarkan id detail order yg diinputkan
    public OrderDetail setOrderDetailDone(Long orderId, Long detailOrderId) {
        Order order = getOrderById(orderId);
        OrderDetail orderDetail = null;

        if (order != null) {
            orderDetail = getOrderDetailById(orderId, detailOrderId);

            if (orderDetail != null) {
                orderDetail.setDone(true);
                responseMessage = "Product successfully set to done.";
                orderDetailRepository.save(orderDetail);
            }
        }

        return orderDetail;
    }

    // Metode untuk menghapus product(detail order) berdasarkan id detail order yg diinputkan
    public boolean deleteOrderDetail(Long orderId, Long detailOrderId) {
        boolean result = true;
        Order order = getOrderById(orderId);

        if (order != null) {
            OrderDetail orderDetail = getOrderDetailById(orderId, detailOrderId);

            if (orderDetail != null && !orderDetail.getDone()) {
                order.removeOrderDetail(orderDetail);
                orderDetailRepository.deleteById(detailOrderId); // delete selected orderDetail from database

                int total = accumulateTotalAmount(order); // recalculate totalAmount after selected orderDetail removed from order
                order.setTotalAmount(total); // assign new calculated total to order obj

                if (order.getMember() != null) {
                    int pointObtained = total / 10; // recalculate point from order after selected orderDetail removed from order
                    order.setPointObtained(pointObtained);
                }

                orderRepository.save(order); // save new order data to database
                responseMessage = "Data order detail successfully removed.";
            } else {
                if (orderDetail != null && orderDetail.getDone()) {
                    responseMessage = "Sorry, order with product ID " + detailOrderId + " has been done.";
                }
                result = false;
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

    // Metode untuk validasi delete order
    private String validateDeleteOrder(Order order) {
        String message = "";
        if (order == null) {
            message = "Sorry Order is Not Found.";
        } else if (order.isPaid()) {
            message = "Sorry, order can't be deleted because order has been paid.";
        } else if (!order.getOrderDetails().isEmpty()) {
            message = "Sorry, can't remove your order because there is products in order with ID 1.";
        }
        return message;
    }

    private String validateOrderDetailData(Long orderId, Long productId, int qty) {
        String message = "";
        Order order = getOrderById(orderId);
        Product product = productService.getProductById(productId);

        if (order == null) message = "Sorry, order with ID " + orderId + " doesn't exist.";
        else if (product == null) message = "Sorry, product with ID " + productId + " doesn't exist.";
        else if (qty < 1) message = "Sorry, quantity must be positive number.";
        else if (order.isPaid()) message = "Sorry, order with ID " + order.getId() + " has been paid.";

        return message;
    }

    // Metode untuk menghitung total pesanan yang harus dibayarkan
    private int accumulateTotalAmount(Order order) {
        int total = 0;

        for (int i = 0; i < order.getOrderDetails().size(); i++) { // iterate through list<OrderDetail> in Order obj
            OrderDetail detail = order.getOrderDetails().get(i);
            int productPrice = detail.getProduct().getPrice();
            int qty = detail.getQty();
            total += (productPrice * qty); // add price * qty to total variable for every iteration
        }

        return total;
    }

    // Metode untuk menghitung total point yang didapatkan member
    private int accumulatePoints(Order order) {
        int pointFromOrder = order.getPointObtained(); // get point value from orderDetail.order
        int pointToAdd = order.getTotalAmount() / 10;

        pointFromOrder += pointToAdd; // accumulate existing point in Order obj with pointToAdd variable

        return pointFromOrder;
    }

    // Metode untuk mendapatkan detail order berdasarkan orderId dan detailOrderId
    private OrderDetail getOrderDetailById(Long orderId, Long detailOrderId) {
        OrderDetail orderDetail = null;
        List<OrderDetail> orderDetails = orderDetailRepository.getOrderDetailsByOrderIdAndDeletedAtIsNullOrderById(orderId);
        int i = 0;

        while (i < orderDetails.size() && orderDetail == null) {
            OrderDetail orderDetailFromLoop = orderDetails.get(i);
            Order order = orderDetailFromLoop.getOrder();

            if (order.getOrderDetails().isEmpty()) {
                responseMessage = "Sorry, order with ID " + orderId + " doesn't have any product yet.";
            } else {
                if (order.getId().equals(orderId) && orderDetailFromLoop.getId().equals(detailOrderId)) {
                    orderDetail = orderDetailFromLoop;
                } else {
                    responseMessage = "Sorry, cannot find Order Detail with ID " + detailOrderId + " on Order with ID " + orderId + ".";
                }
            }

            i++;
        }

        return orderDetail;
    }
}
