package com.shop.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Admin user - implements Singleton pattern.
 */
public class Admin extends UserAccount {
    private static final long serialVersionUID = 1L;

    private static Admin instance;
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin";
    private static final String ADMIN_EMAIL = "admin@shop.com";
    private static final String ADMIN_PHONE = "09000000000";

    private List<Product> productList;
    private List<Request> requestList;

    private Admin() {
        super(ADMIN_USERNAME, ADMIN_EMAIL, ADMIN_PHONE, ADMIN_PASSWORD, true);
        this.productList = new ArrayList<>();
        this.requestList = new ArrayList<>();
    }


    //Returns the single Admin instance.

    public static Admin getInstance() {
        if (instance == null) {
            instance = new Admin();
        }
        return instance;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void addProduct(Product product) {
        if (product == null) throw new IllegalArgumentException("Product cannot be null.");
        productList.add(product);
        System.out.println("Product \"" + product.getName() + "\" was added successfully.");
    }

    public boolean removeProduct(String productId) {
        boolean removed = productList.removeIf(p -> p.getId().equals(productId));
        if (removed) {
            System.out.println("Product with ID " + productId + " was removed.");
        }
        return removed;
    }

    public Product findProductById(String productId) {
        return productList.stream()
                .filter(p -> p.getId().equals(productId))
                .findFirst()
                .orElse(null);
    }

    public List<Request> getRequestList() {
        return requestList;
    }

    public void addRequest(Request request) {
        if (request != null) {
            requestList.add(request);
        }
    }

    public List<Request> getPendingRequests() {
        List<Request> pending = new ArrayList<>();
        for (Request r : requestList) {
            if (r.getStatus() == Request.RequestStatus.PENDING) {
                pending.add(r);
            }
        }
        return pending;
    }

    @Override
    public String toString() {
        return "System Admin | Username: " + getUsername() +
                " | Products: " + productList.size() +
                " | Pending requests: " + getPendingRequests().size();
    }
}
