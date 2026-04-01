package com.euphoria.demo.repository;

import com.euphoria.demo.model.Category;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends MongoRepository<Category, String> {
    Optional<Category> findByName(String id);

    void deleteByName(String id);

    List<Category> findByActive(boolean b);
}
