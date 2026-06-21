package com.shop.service;

import com.shop.model.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for managing comments and ratings.
 */
public class CommentRatingService {
    private final RequestService requestService;

    public CommentRatingService(RequestService requestService) {
        this.requestService = requestService;
    }

    /**
     * Submits a comment on a product. Sends to admin for approval.
     */
    public Comment submitComment(Buyer buyer, Product product, String text) {
        if (buyer == null || product == null || text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException("Comment information is incomplete.");
        }

        boolean hasPurchased = buyer.hasPurchased(product);
        Comment comment = new Comment(buyer, product, text, hasPurchased);
        product.addComment(comment);

        // Send approval request to admin
        Request request = new Request(buyer, comment);
        requestService.submitRequest(request);

        System.out.println("Your comment was submitted and is pending admin approval.");
        return comment;
    }

    /**
     * Submits a rating for a product. Only buyers who purchased can rate.
     */
    public Rating submitRating(Buyer buyer, Product product, int score) {
        if (!buyer.hasPurchased(product)) {
            throw new IllegalStateException("Only buyers who purchased this product can rate it.");
        }
        if (score < 1 || score > 5) {
            throw new IllegalArgumentException("Score must be between 1 and 5.");
        }

        Rating rating = new Rating(buyer, product, score);
        product.addRating(rating);

        System.out.printf("Rating %d/5 submitted for \"%s\".%n", score, product.getName());
        return rating;
    }

    /**
     * Returns only approved comments for a product.
     */
    public List<Comment> getApprovedComments(Product product) {
        return product.getComments().stream()
                .filter(c -> c.getStatus() == Comment.CommentStatus.APPROVED)
                .collect(Collectors.toList());
    }


    public void showApprovedComments(Product product) {
        List<Comment> approved = getApprovedComments(product);
        if (approved.isEmpty()) {
            System.out.println("No comments yet for this product.");
            return;
        }
        System.out.println("============= User Comments =============");
        approved.forEach(System.out::println);
    }
}
