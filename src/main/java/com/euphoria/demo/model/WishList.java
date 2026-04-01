package com.euphoria.demo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "wishlists")
public class WishList {
    @Id
    private String id;
    private String userId;
    private List<WishListItem> products = new ArrayList<>();

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WishListItem {
        private String productId;
        private String name;
        private Double price;
        private String image;
    }
}