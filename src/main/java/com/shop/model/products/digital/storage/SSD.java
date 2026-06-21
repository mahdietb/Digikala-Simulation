package com.shop.model.products.digital.storage;


public class SSD extends StorageDevice {
    private int readSpeedMBps;
    private int writeSpeedMBps;

    public SSD(String name, double price, int stock,
               double weight, String dimensions,
               int capacityGB, int readSpeedMBps, int writeSpeedMBps) {
        super(name, price, stock, weight, dimensions, capacityGB);
        setReadSpeedMBps(readSpeedMBps);
        setWriteSpeedMBps(writeSpeedMBps);
    }

    public int getReadSpeedMBps() { return readSpeedMBps; }

    public void setReadSpeedMBps(int readSpeedMBps) {
        if (readSpeedMBps <= 0) throw new IllegalArgumentException("Read speed must be positive.");
        this.readSpeedMBps = readSpeedMBps;
    }

    public int getWriteSpeedMBps() { return writeSpeedMBps; }

    public void setWriteSpeedMBps(int writeSpeedMBps) {
        if (writeSpeedMBps <= 0) throw new IllegalArgumentException("Write speed must be positive.");
        this.writeSpeedMBps = writeSpeedMBps;
    }

    @Override
    public String getSpecificDetails() {
        return getStorageDetails() +
                "Read speed: " + readSpeedMBps + " MB/s\n" +
                "Write speed: " + writeSpeedMBps + " MB/s\n";
    }

    @Override
    public String toString() {
        return "[SSD] " + super.toString();
    }
}
