package com.shop.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Buyer extends UserAccount {
    private static final long serialVersionUID = 1L;

    /**
     * Tracks whether the buyer's registration has been approved by the admin.
     * Buyers cannot log in until their status is APPROVED.
     */
    public enum RegistrationStatus {
        PENDING("Pending approval"),
        APPROVED("Approved"),
        REJECTED("Rejected");

        private final String displayName;
        RegistrationStatus(String displayName) { this.displayName = displayName; }
        public String getDisplayName() { return displayName; }
    }

    private double accountBalance;
    private Map<Product, Integer> shoppingCart; // Product and its quantity
    private List<Invoice> purchaseHistory;
    private RegistrationStatus registrationStatus;

    public Buyer(String username, String email, String phoneNumber, String password) {
        super(username, email, phoneNumber, password);
        this.accountBalance = 0.0;
        this.shoppingCart = new HashMap<>();
        this.purchaseHistory = new ArrayList<>();
        this.registrationStatus = RegistrationStatus.PENDING;
    }

    public RegistrationStatus getRegistrationStatus() {
        return registrationStatus;
    }

    public void setRegistrationStatus(RegistrationStatus registrationStatus) {
        if (registrationStatus == null) {
            throw new IllegalArgumentException("Registration status cannot be null.");
        }
        this.registrationStatus = registrationStatus;
    }

    public boolean isApproved() {
        return registrationStatus == RegistrationStatus.APPROVED;
    }

    public double getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(double accountBalance) {
        if (accountBalance < 0) {
            throw new IllegalArgumentException("Account balance cannot be negative.");
        }
        this.accountBalance = accountBalance;
    }

    public void addBalance(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Top-up amount must be positive.");
        }
        this.accountBalance += amount;
    }

    public boolean deductBalance(double amount) {
        if (amount <= 0) throw new IllegalArgumentException("Deduction amount must be positive.");
        if (this.accountBalance < amount) return false;
        this.accountBalance -= amount;
        return true;
    }

    public Map<Product, Integer> getShoppingCart() {
        return shoppingCart;
    }

    public void addToCart(Product product, int quantity) {
        if (product == null) throw new IllegalArgumentException("Product cannot be null.");
        if (quantity <= 0) throw new IllegalArgumentException("Quantity must be positive.");
        shoppingCart.merge(product, quantity, Integer::sum);
    }

    public void removeFromCart(Product product) {
        if (product == null) throw new IllegalArgumentException("Product cannot be null.");
        shoppingCart.remove(product);
    }

    public void reduceCartItem(Product product, int quantity) {
        if (!shoppingCart.containsKey(product)) {
            throw new IllegalArgumentException("This product is not in your cart.");
        }
        int current = shoppingCart.get(product);
        if (quantity >= current) {
            shoppingCart.remove(product);
        } else {
            shoppingCart.put(product, current - quantity);
        }
    }

    public void clearCart() {
        shoppingCart.clear();
    }

    public double getCartTotal() {
        return shoppingCart.entrySet().stream()
                .mapToDouble(e -> e.getKey().getPrice() * e.getValue())
                .sum();
    }

    public List<Invoice> getPurchaseHistory() {
        return purchaseHistory;
    }

    public void addInvoice(Invoice invoice) {
        if (invoice != null) {
            purchaseHistory.add(invoice);
        }
    }

    public boolean hasPurchased(Product product) {
        return purchaseHistory.stream()
                .anyMatch(inv -> inv.containsProduct(product));
    }

    @Override
    public String toString() {
        return super.toString() +
                " | Status: " + registrationStatus.getDisplayName() +
                " | Balance: " + String.format("%.0f", accountBalance) + " Toman" +
                " | Invoices: " + purchaseHistory.size();
    }
}
