package com.euphoria.demo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import java.util.List;

@Data
@Document(collection = "products")
public class Product {
    @Id
    private String id; // MongoDB uses Strings for IDs by default
    private String name;
    private String description;
    private Double price;
    private List<String> category;
    private String imageUrl;

    // Gallery support for multiple images/videos
    private List<String> gallery;
    private String videoUrl;

    // Flexible attributes (Weight, Occasion, etc.)
    private List<String> tags;
    private boolean isAvailable = true;
}