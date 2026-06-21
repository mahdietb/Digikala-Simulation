package com.shop.model.products.digital.storage;


public class FlashMemory extends StorageDevice {
    private String usbVersion; // e.g. "USB 3.0"

    public FlashMemory(String name, double price, int stock,
                       double weight, String dimensions,
                       int capacityGB, String usbVersion) {
        super(name, price, stock, weight, dimensions, capacityGB);
        setUsbVersion(usbVersion);
    }

    public String getUsbVersion() { return usbVersion; }

    public void setUsbVersion(String usbVersion) {
        if (usbVersion == null || usbVersion.trim().isEmpty())
            throw new IllegalArgumentException("USB version cannot be empty.");
        this.usbVersion = usbVersion.trim();
    }

    @Override
    public String getSpecificDetails() {
        return getStorageDetails() +
                "USB version: " + usbVersion + "\n";
    }

    @Override
    public String toString() {
        return "[Flash Memory] " + super.toString();
    }
}
