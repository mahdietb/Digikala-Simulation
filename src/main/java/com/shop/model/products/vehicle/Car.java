package com.shop.model.products.vehicle;


public class Car extends Vehicle {
    private int engineVolumeCC;
    private boolean automatic;

    public Car(String name, double price, int stock,
               String manufacturerName, int engineVolumeCC, boolean automatic) {
        super(name, price, stock, manufacturerName);
        setEngineVolumeCC(engineVolumeCC);
        this.automatic = automatic;
    }

    public int getEngineVolumeCC() { return engineVolumeCC; }

    public void setEngineVolumeCC(int engineVolumeCC) {
        if (engineVolumeCC <= 0) throw new IllegalArgumentException("Engine volume must be positive.");
        this.engineVolumeCC = engineVolumeCC;
    }

    public boolean isAutomatic() { return automatic; }

    public void setAutomatic(boolean automatic) { this.automatic = automatic; }

    @Override
    public String getSpecificDetails() {
        return getVehicleDetails() +
                "Engine volume: " + engineVolumeCC + " CC\n" +
                "Transmission: " + (automatic ? "Automatic" : "Manual") + "\n";
    }

    @Override
    public String toString() {
        return "[Car] " + super.toString();
    }
}
