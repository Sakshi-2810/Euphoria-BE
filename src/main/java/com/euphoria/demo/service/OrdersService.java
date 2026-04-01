package com.euphoria.demo.service;

import com.euphoria.demo.model.Orders;
import com.euphoria.demo.repository.OrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrdersService {

    @Autowired
    private OrdersRepository ordersRepository;

    // ✅ Place order
    public Orders placeOrder(Orders order) {
        order.setStatus("PENDING");
        return ordersRepository.save(order);
    }

    // ✅ Get all orders for a user
    public List<Orders> getUserOrders(String userId) {
        return ordersRepository.findByUserId(userId);
    }

    // ✅ Get order by ID
    public Orders getOrderById(String id) {
        return ordersRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    // ✅ Update order status (ADMIN)
    public Orders updateOrderStatus(String orderId, String status) {
        Orders order = getOrderById(orderId);
        order.setStatus(status);
        return ordersRepository.save(order);
    }

    // ✅ Get all orders (ADMIN)
    public List<Orders> getAllOrders() {
        return ordersRepository.findAll();
    }
}