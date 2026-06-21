package com.shop.model.products.vehicle;

import com.shop.model.enums.BikeType;

public class Bicycle extends Vehicle {
    private BikeType bikeType;

    public Bicycle(String name, double price, int stock,
                   String manufacturerName, BikeType bikeType) {
        super(name, price, stock, manufacturerName);
        setBikeType(bikeType);
    }

    public BikeType getBikeType() { return bikeType; }

    public void setBikeType(BikeType bikeType) {
        if (bikeType == null) throw new IllegalArgumentException("Bike type cannot be null.");
        this.bikeType = bikeType;
    }

    @Override
    public String getSpecificDetails() {
        return getVehicleDetails() +
                "Bike type: " + bikeType.getDisplayName() + "\n";
    }

    @Override
    public String toString() {
        return "[Bicycle] " + super.toString();
    }
}
