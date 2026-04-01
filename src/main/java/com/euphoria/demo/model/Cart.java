package com.euphoria.demo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import java.util.List;

@Data
@Document(collection = "carts")
public class Cart {
    @Id
    private String id;
    private String userId;
    private List<CartItem> items;
    private Double totalAmount;

    @Data
    public static class CartItem {
        private String productId;
        private String name;
        private Double price;
        private Integer qty;
        private String image;
    }
}