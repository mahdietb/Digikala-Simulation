package com.shop.model.products.digital.storage;

import com.shop.model.products.digital.DigitalProduct;

/**
 * Adds capacity field.
 */
public abstract class StorageDevice extends DigitalProduct {
    private int capacityGB;

    public StorageDevice(String name, double price, int stock,
                         double weight, String dimensions, int capacityGB) {
        super(name, price, stock, weight, dimensions);
        setCapacityGB(capacityGB);
    }

    public int getCapacityGB() { return capacityGB; }

    public void setCapacityGB(int capacityGB) {
        if (capacityGB <= 0) throw new IllegalArgumentException("Capacity must be positive.");
        this.capacityGB = capacityGB;
    }

    protected String getStorageDetails() {
        return getDigitalDetails() + "Capacity: " + capacityGB + " GB\n";
    }
}
