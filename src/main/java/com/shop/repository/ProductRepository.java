package com.shop.repository;

import com.shop.model.Product;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Repository for managing products.
 */
public class ProductRepository {
    private final List<Product> products = new ArrayList<>();

    public void addProduct(Product product) {
        if (product == null) throw new IllegalArgumentException("Product cannot be null.");
        products.add(product);
    }

    public boolean removeById(String id) {
        return products.removeIf(p -> p.getId().equals(id));
    }

    public Optional<Product> findById(String id) {
        if (id == null) return Optional.empty();
        return products.stream()
                .filter(p -> p.getId().equalsIgnoreCase(id.trim()))
                .findFirst();
    }

    public List<Product> findAll() {
        return new ArrayList<>(products);
    }

    public List<Product> findByCategory(String category) {
        if (category == null || category.trim().isEmpty()) return findAll();
        return products.stream()
                .filter(p -> p.getCategory().equalsIgnoreCase(category.trim()))
                .collect(Collectors.toList());
    }

    public List<Product> searchByName(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) return findAll();
        String lowerKeyword = keyword.trim().toLowerCase();
        return products.stream()
                .filter(p -> p.getName().toLowerCase().contains(lowerKeyword))
                .collect(Collectors.toList());
    }

    public List<Product> findAvailable() {
        return products.stream()
                .filter(Product::isAvailable)
                .collect(Collectors.toList());
    }

    public List<Product> findByPriceRange(double minPrice, double maxPrice) {
        return products.stream()
                .filter(p -> p.getPrice() >= minPrice && p.getPrice() <= maxPrice)
                .collect(Collectors.toList());
    }

    public List<Product> findByMinRating(double minRating) {
        return products.stream()
                .filter(p -> p.getAverageRating() >= minRating)
                .collect(Collectors.toList());
    }

    /**
     * Returns paginated results.
     * @param allProducts full list to paginate
     * @param page 1-based page number
     * @param pageSize number of items per page
     */
    public static List<Product> paginate(List<Product> allProducts, int page, int pageSize) {
        if (page < 1) page = 1;
        int fromIndex = (page - 1) * pageSize;
        if (fromIndex >= allProducts.size()) return new ArrayList<>();
        int toIndex = Math.min(fromIndex + pageSize, allProducts.size());
        return allProducts.subList(fromIndex, toIndex);
    }

    public static int getTotalPages(List<Product> allProducts, int pageSize) {
        if (allProducts.isEmpty()) return 0;
        return (int) Math.ceil((double) allProducts.size() / pageSize);
    }

    public int count() {
        return products.size();
    }
}
