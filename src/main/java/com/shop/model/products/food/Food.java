package com.shop.model.products.food;

import com.shop.model.Product;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class Food extends Product {
    public static final String CATEGORY = "Food";

    private LocalDate productionDate;
    private LocalDate expiryDate;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    public Food(String name, double price, int stock,
                LocalDate productionDate, LocalDate expiryDate) {
        super(name, price, stock, CATEGORY);
        setProductionDate(productionDate);
        setExpiryDate(expiryDate);
    }

    public LocalDate getProductionDate() { return productionDate; }

    public void setProductionDate(LocalDate productionDate) {
        if (productionDate == null) throw new IllegalArgumentException("Production date cannot be null.");
        this.productionDate = productionDate;
    }

    public LocalDate getExpiryDate() { return expiryDate; }

    public void setExpiryDate(LocalDate expiryDate) {
        if (expiryDate == null) throw new IllegalArgumentException("Expiry date cannot be null.");
        if (productionDate != null && expiryDate.isBefore(productionDate))
            throw new IllegalArgumentException("Expiry date cannot be before the production date.");
        this.expiryDate = expiryDate;
    }

    public boolean isExpired() {
        return LocalDate.now().isAfter(expiryDate);
    }

    @Override
    public String getSpecificDetails() {
        return "Production date: " + productionDate.format(FORMATTER) + "\n" +
                "Expiry date: " + expiryDate.format(FORMATTER) + "\n" +
                (isExpired() ? "WARNING: This product has expired!\n" : "");
    }

    @Override
    public String toString() {
        return "[Food] " + super.toString();
    }
}
