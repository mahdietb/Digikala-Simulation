package com.shop.model.products.stationery;

import com.shop.model.Product;

/**
 * Adds country of manufacture.
 */
public abstract class Stationery extends Product {
    public static final String CATEGORY = "Stationery";

    private String manufacturingCountry;

    public Stationery(String name, double price, int stock, String manufacturingCountry) {
        super(name, price, stock, CATEGORY);
        setManufacturingCountry(manufacturingCountry);
    }

    public String getManufacturingCountry() { return manufacturingCountry; }

    public void setManufacturingCountry(String country) {
        if (country == null || country.trim().isEmpty())
            throw new IllegalArgumentException("Country of manufacture cannot be empty.");
        this.manufacturingCountry = country.trim();
    }

    protected String getStationeryDetails() {
        return "Country of manufacture: " + manufacturingCountry + "\n";
    }
}
