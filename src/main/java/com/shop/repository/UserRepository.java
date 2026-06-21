package com.shop.repository;

import com.shop.model.Buyer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Repository for managing buyer accounts.
 */
public class UserRepository {
    private final List<Buyer> buyers = new ArrayList<>();

    public void addBuyer(Buyer buyer) {
        if (buyer == null) throw new IllegalArgumentException("Buyer cannot be null.");
        buyers.add(buyer);
    }

    public Optional<Buyer> findByUsername(String username) {
        if (username == null) return Optional.empty();
        return buyers.stream()
                .filter(b -> b.getUsername().equalsIgnoreCase(username.trim()))
                .findFirst();
    }

    public Optional<Buyer> findByEmail(String email) {
        if (email == null) return Optional.empty();
        return buyers.stream()
                .filter(b -> b.getEmail().equalsIgnoreCase(email.trim()))
                .findFirst();
    }

    public boolean existsByUsername(String username) {
        return findByUsername(username).isPresent();
    }

    public boolean existsByEmail(String email) {
        return findByEmail(email).isPresent();
    }

    public List<Buyer> getAllBuyers() {
        return new ArrayList<>(buyers);
    }

    public int count() {
        return buyers.size();
    }
}
