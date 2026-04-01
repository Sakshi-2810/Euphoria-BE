package com.euphoria.demo.service;

import com.euphoria.demo.exception.CustomDataException;
import com.euphoria.demo.model.WishList;
import com.euphoria.demo.repository.WishlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class WishListService {

    @Autowired
    private WishlistRepository wishListRepository;

    // ✅ Get or create wishlist for user
    public WishList getWishList(String userId) {
        if(userId == null || userId.isEmpty()) {
            throw new CustomDataException("User ID cannot be null or empty");
        }
        return wishListRepository.findByUserId(userId).orElseGet(() -> {
            WishList wishlist = new WishList();
            wishlist.setUserId(userId);
            wishlist.setProducts(new ArrayList<>());
            return wishListRepository.save(wishlist);
        });
    }

    // ✅ Add product
    public WishList addProduct(String userId, WishList.WishListItem product) {
        WishList wishlist = getWishList(userId);

        boolean exists = wishlist.getProducts().stream().anyMatch(p -> p.getProductId().equals(product.getProductId()));

        if (!exists) {
            wishlist.getProducts().add(product);
        }

        return wishListRepository.save(wishlist);
    }

    // ✅ Remove product
    public WishList removeProduct(String userId, String productId) {
        WishList wishlist = getWishList(userId);

        wishlist.setProducts(wishlist.getProducts().stream().filter(p -> !p.getProductId().equals(productId)).toList());

        return wishListRepository.save(wishlist);
    }

    // ✅ Clear wishlist
    public WishList clearWishList(String userId) {
        WishList wishlist = getWishList(userId);
        wishlist.setProducts(new ArrayList<>());
        return wishListRepository.save(wishlist);
    }
}