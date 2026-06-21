package com.shop.model.products.digital;


public class PersonalComputer extends DigitalProduct {
    private String processorModel;
    private int ramCapacityGB;

    public PersonalComputer(String name, double price, int stock,
                            double weight, String dimensions,
                            String processorModel, int ramCapacityGB) {
        super(name, price, stock, weight, dimensions);
        setProcessorModel(processorModel);
        setRamCapacityGB(ramCapacityGB);
    }

    public String getProcessorModel() { return processorModel; }

    public void setProcessorModel(String processorModel) {
        if (processorModel == null || processorModel.trim().isEmpty())
            throw new IllegalArgumentException("Processor model cannot be empty.");
        this.processorModel = processorModel.trim();
    }

    public int getRamCapacityGB() { return ramCapacityGB; }

    public void setRamCapacityGB(int ramCapacityGB) {
        if (ramCapacityGB <= 0) throw new IllegalArgumentException("RAM capacity must be positive.");
        this.ramCapacityGB = ramCapacityGB;
    }

    @Override
    public String getSpecificDetails() {
        return getDigitalDetails() +
                "Processor model: " + processorModel + "\n" +
                "RAM capacity: " + ramCapacityGB + " GB\n";
    }

    @Override
    public String toString() {
        return "[Personal Computer] " + super.toString();
    }
}
