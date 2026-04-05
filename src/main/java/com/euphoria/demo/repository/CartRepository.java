package com.euphoria.demo.repository;

import com.euphoria.demo.model.Cart;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends MongoRepository<Cart, String> {
    Optional<Cart> findByUserId(String userId);

    boolean existsByUserId(String userId);

    List<Cart> findByItemsProductIdIn(List<String> id);
}
