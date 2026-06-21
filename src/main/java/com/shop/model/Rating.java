package com.shop.model;

import java.io.Serializable;

/**
 * Rating given by a buyer to a product they purchased.
 */
public class Rating implements Serializable {
    private static final long serialVersionUID = 1L;

    private Buyer buyer;
    private Product product;
    private int score;

    public Rating(Buyer buyer, Product product, int score) {
        setBuyer(buyer);
        setProduct(product);
        setScore(score);
    }

    public Buyer getBuyer() { return buyer; }

    public void setBuyer(Buyer buyer) {
        if (buyer == null) throw new IllegalArgumentException("Buyer cannot be null.");
        this.buyer = buyer;
    }

    public Product getProduct() { return product; }

    public void setProduct(Product product) {
        if (product == null) throw new IllegalArgumentException("Product cannot be null.");
        this.product = product;
    }

    public int getScore() { return score; }

    public void setScore(int score) {
        if (score < 1 || score > 5)
            throw new IllegalArgumentException("Score must be between 1 and 5.");
        this.score = score;
    }

    @Override
    public String toString() {
        return "Rating " + score + "/5 by " + buyer.getUsername() + " for " + product.getName();
    }
}
