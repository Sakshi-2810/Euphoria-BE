package com.euphoria.demo.service;

import com.euphoria.demo.exception.CustomDataException;
import com.euphoria.demo.model.Cart;
import com.euphoria.demo.repository.CartRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Slf4j
@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    // ✅ Get or Create Cart
    public Cart getCart(String userId) {
        if(userId == null || userId.isEmpty()) {
            throw new CustomDataException("User ID cannot be null or empty");
        }
        return cartRepository.findByUserId(userId).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUserId(userId);
            newCart.setItems(new ArrayList<>());
            newCart.setTotalAmount(0.0);
            return cartRepository.save(newCart);
        });
    }

    // ✅ Add Item to Cart
    public Cart addItem(String userId, Cart.CartItem newItem) {

        Cart cart = getCart(userId);
        Optional<Cart.CartItem> existingItem = cart.getItems().stream().filter(item -> item.getProductId().equals(newItem.getProductId())).findFirst();

        if (existingItem.isPresent()) {
            Cart.CartItem item = existingItem.get();
            item.setQty(item.getQty() + newItem.getQty());
        } else {
            cart.getItems().add(newItem);
        }

        updateTotal(cart);
        log.info("Added item to cart: userId={}, productId={}, qty={}", userId, newItem.getProductId(), newItem.getQty());
        return cartRepository.save(cart);
    }

    // ✅ Update Item Quantity
    public Cart updateItem(String userId, String productId, int qty) {

        Cart cart = getCart(userId);

        cart.getItems().forEach(item -> {
            if (item.getProductId().equals(productId)) {
                item.setQty(qty);
            }
        });

        updateTotal(cart);
        log.info("Updated item in cart: userId={}, productId={}, newQty={}", userId, productId, qty);
        return cartRepository.save(cart);
    }

    // ✅ Remove Item
    public Cart removeItem(String userId, String productId) {

        Cart cart = getCart(userId);

        cart.setItems(cart.getItems().stream().filter(item -> !item.getProductId().equals(productId)).toList());

        updateTotal(cart);
        log.info("Removed item from cart: userId={}, productId={}", userId, productId);
        return cartRepository.save(cart);
    }

    // ✅ Clear Cart
    public Cart clearCart(String userId) {

        Cart cart = getCart(userId);
        cart.setItems(new ArrayList<>());
        cart.setTotalAmount(0.0);
        log.info("Cleared cart: userId={}", userId);
        return cartRepository.save(cart);
    }

    // ✅ Calculate Total
    private void updateTotal(Cart cart) {

        double total = cart.getItems().stream().mapToDouble(item -> item.getPrice() * item.getQty()).sum();

        cart.setTotalAmount(total);
    }
}