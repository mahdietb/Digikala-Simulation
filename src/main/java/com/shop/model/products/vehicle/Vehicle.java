package com.shop.model.products.vehicle;

import com.shop.model.Product;

/**
 * Adds manufacturer name.
 */
public abstract class Vehicle extends Product {
    public static final String CATEGORY = "Vehicle";

    private String manufacturerName;

    public Vehicle(String name, double price, int stock, String manufacturerName) {
        super(name, price, stock, CATEGORY);
        setManufacturerName(manufacturerName);
    }

    public String getManufacturerName() { return manufacturerName; }

    public void setManufacturerName(String manufacturerName) {
        if (manufacturerName == null || manufacturerName.trim().isEmpty())
            throw new IllegalArgumentException("Manufacturer name cannot be empty.");
        this.manufacturerName = manufacturerName.trim();
    }

    protected String getVehicleDetails() {
        return "Manufacturer: " + manufacturerName + "\n";
    }
}
