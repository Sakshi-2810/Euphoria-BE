package com.euphoria.demo.controller;

import com.euphoria.demo.dto.Response;
import com.euphoria.demo.model.Product;
import com.euphoria.demo.service.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<Response> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(new Response("All products fetched", products));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getProduct(@PathVariable String id) {
        Product product = productService.getProductById(id);
        return ResponseEntity.ok(new Response("Product fetched", product));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Response> addOrUpdateProduct(@RequestBody Product product) {
        Product saved = productService.saveProduct(product);
        return ResponseEntity.ok(new Response("Product saved", saved));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deleteProduct(@PathVariable String id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(new Response("Product deleted", null));
    }

    @GetMapping("/search")
    public ResponseEntity<Response> searchByName(@RequestParam String name) {
        List<Product> products = productService.searchByName(name);
        return ResponseEntity.ok(new Response("Products found", products));
    }

    @GetMapping("/filter")
    public ResponseEntity<Response> filterByCategory(@RequestParam List<String> categories) {
        List<Product> products = productService.filterByCategory(categories);
        return ResponseEntity.ok(new Response("Products filtered", products));
    }
}