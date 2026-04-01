package com.euphoria.demo.model;

import jakarta.validation.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import java.util.List;
import java.time.LocalDateTime;

@Data
@Document(collection = "orders")
public class Orders {
    @Id
    private String id;
    private String userId; // The customer who placed the order
    @NotEmpty
    private List<OrderItem> items; // A snapshot of the items at time of purchase
    private Double totalAmount;
    private String status; // e.g., "PENDING", "SHIPPED", "DELIVERED", "CANCELLED"
    private ShippingAddress shippingAddress;
    private String paymentMethod; // e.g., "UPI", "CREDIT_CARD", "COD"
    private LocalDateTime orderDate = LocalDateTime.now();
    @Data
    public static class OrderItem {
        private String productId;
        private String name;
        private Double price;
        private Integer qty;
        private String image;
    }

    @Data
    public static class ShippingAddress {
        private String fullName;
        private String phone;
        private String addressLine;
        private String city;
        private String pincode;
        private String state;
    }
}