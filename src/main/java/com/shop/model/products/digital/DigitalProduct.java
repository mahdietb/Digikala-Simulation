package com.shop.model.products.digital;

import com.shop.model.Product;


public abstract class DigitalProduct extends Product {
    public static final String CATEGORY = "Digital";

    private double weight;   // in grams
    private String dimensions; // e.g. "150x70x10 mm"

    public DigitalProduct(String name, double price, int stock, double weight, String dimensions) {
        super(name, price, stock, CATEGORY);
        setWeight(weight);
        this.dimensions = dimensions;
    }

    public double getWeight() { return weight; }

    public void setWeight(double weight) {
        if (weight < 0) throw new IllegalArgumentException("Weight cannot be negative.");
        this.weight = weight;
    }

    public String getDimensions() { return dimensions; }

    public void setDimensions(String dimensions) {
        this.dimensions = dimensions;
    }

    protected String getDigitalDetails() {
        return "Weight: " + weight + " g\n" +
                "Dimensions: " + dimensions + "\n";
    }
}
