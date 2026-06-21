package com.shop.model.enums;

/**
 * Enum for pencil types.
 */
public enum PencilType {
    HB("HB"),
    B("B"),
    F("F"),
    H("H"),
    H2("2H");

    private final String displayName;

    PencilType(String displayName) {
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
