package com.shop.model;

import java.io.Serializable;

/**
 * Comment left by a user on a product.
 */
public class Comment implements Serializable {
    private static final long serialVersionUID = 1L;

    public enum CommentStatus {
        PENDING("Pending approval"),
        APPROVED("Approved"),
        REJECTED("Rejected");

        private final String displayName;
        CommentStatus(String displayName) { this.displayName = displayName; }
        public String getDisplayName() { return displayName; }
    }

    private static int counter = 1;

    private int id;
    private UserAccount author;
    private Product product;
    private String text;
    private CommentStatus status;
    private boolean hasPurchased; // whether the user who write the comment bought the product

    public Comment(UserAccount author, Product product, String text, boolean hasPurchased) {
        this.id = counter++;
        setAuthor(author);
        setProduct(product);
        setText(text);
        this.hasPurchased = hasPurchased;
        this.status = CommentStatus.PENDING;
    }

    public int getId() { return id; }

    public UserAccount getAuthor() { return author; }

    public void setAuthor(UserAccount author) {
        if (author == null) throw new IllegalArgumentException("Comment author cannot be null.");
        this.author = author;
    }

    public Product getProduct() { return product; }

    public void setProduct(Product product) {
        if (product == null) throw new IllegalArgumentException("Product cannot be null.");
        this.product = product;
    }

    public String getText() { return text; }

    public void setText(String text) {
        if (text == null || text.trim().isEmpty())
            throw new IllegalArgumentException("Comment text cannot be empty.");
        this.text = text.trim();
    }

    public CommentStatus getStatus() { return status; }

    public void setStatus(CommentStatus status) {
        if (status == null) throw new IllegalArgumentException("Status cannot be null.");
        this.status = status;
    }

    public boolean isHasPurchased() { return hasPurchased; }

    @Override
    public String toString() {
        return "----------------------------------\n" +
                "Comment #" + id + "\n" +
                "Author: " + author.getUsername() +
                (hasPurchased ? " [Verified buyer]" : "") + "\n" +
                "Status: " + status.getDisplayName() + "\n" +
                "Text: " + text + "\n" +
                "----------------------------------";
    }
}
