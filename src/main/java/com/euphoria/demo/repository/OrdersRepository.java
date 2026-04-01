package com.euphoria.demo.repository;

import com.euphoria.demo.model.Orders;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface OrdersRepository extends MongoRepository<Orders, String> {
    List<Orders> findByUserId(String userId);
}
