package com.shop.model.enums;

/**
 * Enum for bicycle types.
 */
public enum BikeType {
    MOUNTAIN("Mountain"),
    ROAD("Road"),
    CITY("City"),
    HYBRID("Hybrid");

    private final String displayName;

    BikeType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
