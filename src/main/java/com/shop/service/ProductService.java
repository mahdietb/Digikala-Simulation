package com.shop.service;

import com.shop.model.Admin;
import com.shop.model.Product;
import com.shop.repository.ProductRepository;

import java.util.List;

/**
 * Service for product management (admin operations and browsing).
 */
public class ProductService {
    private final ProductRepository productRepository;
    private final Admin admin;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
        this.admin = Admin.getInstance();
    }

    public void addProduct(Product product) {
        productRepository.addProduct(product);
        admin.addProduct(product);
    }

    public boolean removeProduct(String productId) {
        boolean removed = productRepository.removeById(productId);
        if (removed) {
            admin.removeProduct(productId);
        }
        return removed;
    }

    public Product getProductById(String id) {
        return productRepository.findById(id).orElse(null);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategory(category);
    }

    public List<Product> searchProducts(String keyword) {
        return productRepository.searchByName(keyword);
    }

    public List<Product> filterAvailable() {
        return productRepository.findAvailable();
    }

    public List<Product> filterByPriceRange(double min, double max) {
        return productRepository.findByPriceRange(min, max);
    }

    public List<Product> filterByMinRating(double minRating) {
        return productRepository.findByMinRating(minRating);
    }

    /**
     * Returns paginated products.
     */
    public List<Product> getPage(List<Product> products, int page, int pageSize) {
        return ProductRepository.paginate(products, page, pageSize);
    }

    public int getTotalPages(List<Product> products, int pageSize) {
        return ProductRepository.getTotalPages(products, pageSize);
    }

    /**
     * Admin can only edit name, price, stock.
     */
    public boolean editProduct(String productId, String newName, Double newPrice, Integer newStock) {
        Product product = getProductById(productId);
        if (product == null) {
            System.out.println("Product with ID " + productId + " not found.");
            return false;
        }
        if (newName != null && !newName.trim().isEmpty()) {
            product.setName(newName.trim());
        }
        if (newPrice != null) {
            product.setPrice(newPrice);
        }
        if (newStock != null) {
            product.setStock(newStock);
        }
        System.out.println("Product \"" + product.getName() + "\" updated.");
        return true;
    }
}
