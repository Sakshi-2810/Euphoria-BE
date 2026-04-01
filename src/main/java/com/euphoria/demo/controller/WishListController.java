package com.euphoria.demo.controller;

import com.euphoria.demo.dto.Response;
import com.euphoria.demo.model.WishList;
import com.euphoria.demo.service.WishListService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wishlist")
public class WishListController {

    @Autowired
    private WishListService wishListService;

    private String getCurrentUserId() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    // ✅ Get wishlist
    @GetMapping
    public ResponseEntity<Response> getWishList() {
        String userId = getCurrentUserId();
        WishList wishlist = wishListService.getWishList(userId);
        return ResponseEntity.ok(new Response("Wishlist fetched", wishlist));
    }

    // ✅ Add product
    @PostMapping("/add")
    public ResponseEntity<Response> addProduct(@RequestBody WishList.WishListItem product) {
        String userId = getCurrentUserId();
        WishList wishlist = wishListService.addProduct(userId, product);
        return ResponseEntity.ok(new Response("Product added", wishlist));
    }

    // ✅ Remove product
    @DeleteMapping("/remove")
    public ResponseEntity<Response> removeProduct(@RequestParam String productId) {
        String userId = getCurrentUserId();
        WishList wishlist = wishListService.removeProduct(userId, productId);
        return ResponseEntity.ok(new Response("Product removed", wishlist));
    }

    // ✅ Clear wishlist
    @DeleteMapping("/clear")
    public ResponseEntity<Response> clearWishList() {
        String userId = getCurrentUserId();
        WishList wishlist = wishListService.clearWishList(userId);
        return ResponseEntity.ok(new Response("Wishlist cleared", wishlist));
    }
}