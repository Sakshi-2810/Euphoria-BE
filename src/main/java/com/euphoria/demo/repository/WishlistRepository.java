package com.euphoria.demo.repository;

import com.euphoria.demo.model.WishList;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface WishlistRepository extends MongoRepository<WishList, String> {
    Optional<WishList> findByUserId(String userId);
}
