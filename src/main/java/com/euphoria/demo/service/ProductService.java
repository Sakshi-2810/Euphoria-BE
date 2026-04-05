package com.euphoria.demo.service;

import com.euphoria.demo.model.Cart;
import com.euphoria.demo.model.Product;
import com.euphoria.demo.repository.CartRepository;
import com.euphoria.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    CartRepository cartRepository;
    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(String id) {
        return productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public Product saveProduct(Product product) {
        if (product.getImage().isBlank() && !product.getGallery().isEmpty()) {
            product.setImage(product.getGallery().get(0));
        }
        return productRepository.save(product);
    }

    public void deleteProduct(String id) {
        productRepository.deleteById(id);
        List<Cart> carts = cartRepository.findByItemsProductIdIn(List.of(id));

        carts.forEach(cart -> {
            List<Cart.CartItem> updatedItems = cart.getItems().stream().filter(item -> !item.getProductId().equals(id)).collect(Collectors.toList());
            cart.setItems(updatedItems);
        });

        cartRepository.saveAll(carts);
    }

    public List<Product> searchByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name);
    }

    public List<Product> filterByCategory(List<String> categories) {
        return productRepository.findByCategoryIn(categories);
    }
}