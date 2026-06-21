package com.shop.model.products.stationery;

import com.shop.model.enums.PencilType;

public class Pencil extends Stationery {
    private PencilType pencilType;

    public Pencil(String name, double price, int stock,
                  String manufacturingCountry, PencilType pencilType) {
        super(name, price, stock, manufacturingCountry);
        setPencilType(pencilType);
    }

    public PencilType getPencilType() { return pencilType; }

    public void setPencilType(PencilType pencilType) {
        if (pencilType == null) throw new IllegalArgumentException("Pencil type cannot be null.");
        this.pencilType = pencilType;
    }

    @Override
    public String getSpecificDetails() {
        return getStationeryDetails() +
                "Pencil type: " + pencilType.getDisplayName() + "\n";
    }

    @Override
    public String toString() {
        return "[Pencil] " + super.toString();
    }
}
