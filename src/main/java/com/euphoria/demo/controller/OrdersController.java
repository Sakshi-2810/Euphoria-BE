package com.euphoria.demo.controller;

import com.euphoria.demo.dto.Response;
import com.euphoria.demo.model.Orders;
import com.euphoria.demo.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrdersController {

    @Autowired
    private OrdersService ordersService;

    private String getCurrentUserId() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    // ✅ Place order
    @PostMapping("/place")
    public ResponseEntity<Response> placeOrder(@RequestBody Orders order) {
        order.setUserId(getCurrentUserId());
        Orders savedOrder = ordersService.placeOrder(order);
        return ResponseEntity.ok(new Response("Order placed successfully", savedOrder));
    }

    // ✅ Get all orders for current user
    @GetMapping
    public ResponseEntity<Response> getUserOrders() {
        String userId = getCurrentUserId();
        List<Orders> orders = ordersService.getUserOrders(userId);
        return ResponseEntity.ok(new Response("User orders fetched", orders));
    }

    // ✅ Get order by ID (user/admin)
    @GetMapping("/{id}")
    public ResponseEntity<Response> getOrder(@PathVariable String id) {
        Orders order = ordersService.getOrderById(id);
        return ResponseEntity.ok(new Response("Order fetched", order));
    }

    // ✅ Admin: update order status
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/status")
    public ResponseEntity<Response> updateOrderStatus(@PathVariable String id,
                                                      @RequestParam String status) {
        Orders updated = ordersService.updateOrderStatus(id, status);
        return ResponseEntity.ok(new Response("Order status updated", updated));
    }

    // ✅ Admin: get all orders
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<Response> getAllOrders() {
        List<Orders> allOrders = ordersService.getAllOrders();
        return ResponseEntity.ok(new Response("All orders fetched", allOrders));
    }
}