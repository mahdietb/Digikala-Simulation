package com.shop.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public abstract class Product implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private double price;
    private int stock;
    private List<Comment> comments;
    private List<Rating> ratings;
    private String category;

    public Product(String name, double price, int stock, String category) {
        this.id = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        setName(name);
        setPrice(price);
        setStock(stock);
        this.category = category;
        this.comments = new ArrayList<>();
        this.ratings = new ArrayList<>();
    }

    public String getId() { return id; }

    public String getName() { return name; }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty())
            throw new IllegalArgumentException("Product name cannot be empty.");
        this.name = name.trim();
    }

    public double getPrice() { return price; }

    public void setPrice(double price) {
        if (price < 0) throw new IllegalArgumentException("Price cannot be negative.");
        this.price = price;
    }

    public int getStock() { return stock; }

    public void setStock(int stock) {
        if (stock < 0) throw new IllegalArgumentException("Stock cannot be negative.");
        this.stock = stock;
    }

    public boolean isAvailable() { return stock > 0; }

    public void decreaseStock(int quantity) {
        if (quantity > stock)
            throw new IllegalStateException("Not enough stock. Current stock: " + stock);
        this.stock -= quantity;
    }

    public String getCategory() { return category; }

    public List<Comment> getComments() { return comments; }

    public void addComment(Comment comment) {
        if (comment != null) comments.add(comment);
    }

    /**
     * Returns the number of comments visible to regular users. only
     * those approved by the admin.
     */
    public long getApprovedCommentCount() {
        return comments.stream()
                .filter(c -> c.getStatus() == Comment.CommentStatus.APPROVED)
                .count();
    }

    public List<Rating> getRatings() { return ratings; }

    public void addRating(Rating rating) {
        if (rating == null) return;
        // Remove previous rating from same user
        ratings.removeIf(r -> r.getBuyer().getUsername().equals(rating.getBuyer().getUsername()));
        ratings.add(rating);
    }

    public double getAverageRating() {
        if (ratings.isEmpty()) return 0.0;
        return ratings.stream().mapToDouble(Rating::getScore).average().orElse(0.0);
    }

    /**
     * Returns product-specific details as a formatted string.
     * Subclasses must implement this.
     */
    public abstract String getSpecificDetails();

    @Override
    public String toString() {
        return "----------------------------------\n" +
                "ID: " + id + "\n" +
                "Name: " + name + "\n" +
                "Category: " + category + "\n" +
                "Price: " + String.format("%.0f", price) + " Toman\n" +
                "Stock status: " + (isAvailable() ? "In stock (" + stock + " units)" : "Out of stock") + "\n" +
                "Average rating: " + String.format("%.1f", getAverageRating()) + " / 5\n" +
                "Comment count: " + getApprovedCommentCount() + "\n" +
                getSpecificDetails() +
                "----------------------------------";
    }
}
