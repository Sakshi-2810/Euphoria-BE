package com.euphoria.demo.controller;

import com.euphoria.demo.dto.Response;
import com.euphoria.demo.model.Category;
import com.euphoria.demo.service.CategoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    // ✅ Get all categories (public)
    @GetMapping
    public ResponseEntity<Response> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(new Response("All categories fetched", categories));
    }

    // ✅ Get category by ID (public)
    @GetMapping("/{id}")
    public ResponseEntity<Response> getCategory(@PathVariable String id) {
        Category category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(new Response("Category fetched", category));
    }

    // ✅ Add / update category (ADMIN only)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Response> addOrUpdateCategory(@RequestBody Category category) {
        Category saved = categoryService.saveCategory(category);
        return ResponseEntity.ok(new Response("Category saved", saved));
    }

    // ✅ Delete category (ADMIN only)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deleteCategory(@PathVariable String id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok(new Response("Category deleted", null));
    }
}