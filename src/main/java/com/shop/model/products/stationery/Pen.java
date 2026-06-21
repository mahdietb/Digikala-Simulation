package com.shop.model.products.stationery;


public class Pen extends Stationery {
    private String color;

    public Pen(String name, double price, int stock,
               String manufacturingCountry, String color) {
        super(name, price, stock, manufacturingCountry);
        setColor(color);
    }

    public String getColor() { return color; }

    public void setColor(String color) {
        if (color == null || color.trim().isEmpty())
            throw new IllegalArgumentException("Pen color cannot be empty.");
        this.color = color.trim();
    }

    @Override
    public String getSpecificDetails() {
        return getStationeryDetails() +
                "Color: " + color + "\n";
    }

    @Override
    public String toString() {
        return "[Pen] " + super.toString();
    }
}
