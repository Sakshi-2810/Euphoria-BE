package com.euphoria.demo.service;

import com.euphoria.demo.dto.Response;
import com.euphoria.demo.exception.CustomDataException;
import com.euphoria.demo.model.Cart;
import com.euphoria.demo.model.Orders;
import com.euphoria.demo.repository.CartRepository;
import com.euphoria.demo.repository.OrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class OrdersService {

    @Autowired
    private OrdersRepository ordersRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    // ✅ Place order
    public Response placeOrder(Orders order) {
        order.setStatus("PENDING");
        Cart cart = cartRepository.findByUserId(order.getUserId()).orElseThrow(() -> new RuntimeException("Cart not found for user"));
        cart.setItems(cart.getItems().stream().filter(item -> !order.getItems().stream().anyMatch(orderItem -> orderItem.getProductId().equals(item.getProductId()))).toList());
        cartRepository.save(cart);
        Orders savedOrder = ordersRepository.save(order);
        messagingTemplate.convertAndSend("/topic/orders", Optional.of(Map.of("id", savedOrder.getId(), "message", "New Order #" + savedOrder.getId())));
        return new Response("Order placed successfully", order);
    }

    // ✅ Get all orders for a user
    public List<Orders> getUserOrders(String userId) {
        return ordersRepository.findByUserId(userId);
    }

    // ✅ Get order by ID
    public Orders getOrderById(String id) {
        return ordersRepository.findById(id).orElseThrow(() -> new CustomDataException("Order not found"));
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

    public Response cancelOrder(String orderId, String userId) {
        Orders order = getOrderById(orderId);
        if (!order.getUserId().equals(userId)) {
            throw new CustomDataException("Unauthorized to cancel this order");
        }
        if (order.getStatus().equals("PENDING")) {
            order.setStatus("CANCELED");
            ordersRepository.save(order);
            return new Response("Order canceled successfully", order);
        } else {
            throw new CustomDataException("Only pending orders can be canceled");
        }
    }
}