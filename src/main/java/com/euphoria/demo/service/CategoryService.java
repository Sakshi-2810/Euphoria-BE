package com.euphoria.demo.service;

import com.euphoria.demo.exception.CustomDataException;
import com.euphoria.demo.model.Category;
import com.euphoria.demo.repository.CategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    // ✅ Get all categories
    public List<Category> getAllCategories() {
        log.info("Fetching all active categories");
        return categoryRepository.findByActive(true);
    }

    // ✅ Get by ID
    public Category getCategoryById(String id) {
        log.info("Fetching category by ID: {}", id);
        return categoryRepository.findByName(id).orElseThrow(() -> new CustomDataException("Category not found"));
    }

    // ✅ Add / update category
    public Category saveCategory(Category category) {
        category.setActive(true); // Ensure new categories are active by default
        log.info("Saving category: {}", category.getName());
        return categoryRepository.save(category);
    }

    // ✅ Delete category
    public void deleteCategory(String id) {
        Category category = categoryRepository.findByName(id).orElseThrow(() -> new CustomDataException("Category not found"));
        category.setActive(false); // Soft delete by marking as inactive
        log.info("Soft deleting category: {}", id);
        categoryRepository.save(category);
    }
}