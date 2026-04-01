package com.euphoria.demo.controller;

import com.euphoria.demo.dto.Response;
import com.euphoria.demo.model.Cart;
import com.euphoria.demo.security.JwtUtil;
import com.euphoria.demo.service.CartService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping
    public ResponseEntity<Response> getCart(HttpServletRequest request) {
        String userId = jwtUtil.extractEmailFromRequest(request);
        Cart cart = cartService.getCart(userId);
        return ResponseEntity.ok(new Response("Cart fetched", cart));
    }

    @PostMapping("/add")
    public ResponseEntity<Response> addItem(HttpServletRequest request, @RequestBody Cart.CartItem item) {
        String userId = jwtUtil.extractEmailFromRequest(request);
        Cart cart = cartService.addItem(userId, item);
        return ResponseEntity.ok(new Response("Item added", cart));
    }

    // ✅ Update Item
    @PutMapping("/update")
    public ResponseEntity<Response> updateItem(HttpServletRequest request, @RequestParam String productId, @RequestParam int qty) {
        String userId = jwtUtil.extractEmailFromRequest(request);
        Cart cart = cartService.updateItem(userId, productId, qty);
        return ResponseEntity.ok(new Response("Item updated", cart));
    }

    // ✅ Remove Item
    @DeleteMapping("/remove")
    public ResponseEntity<Response> removeItem(HttpServletRequest request, @RequestParam String productId) {
        String userId = jwtUtil.extractEmailFromRequest(request);
        Cart cart = cartService.removeItem(userId, productId);
        return ResponseEntity.ok(new Response("Item removed", cart));
    }

    // ✅ Clear Cart
    @DeleteMapping("/clear")
    public ResponseEntity<Response> clearCart(HttpServletRequest request) {
        String userId = jwtUtil.extractEmailFromRequest(request);
        Cart cart = cartService.clearCart(userId);
        return ResponseEntity.ok(new Response("Cart cleared", cart));
    }
}