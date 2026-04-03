package com.euphoria.demo.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "carts")
public class Cart {
    @Id
    private String userId;
    private List<CartItem> items;
    private Double totalAmount;

    @Data
    public static class CartItem {
        @NotBlank
        private String productId;
        private String name;
        private Double price;
        private Integer qty;
        private String image;
    }
}