package com.revolt.enums;

public enum Currency {

    GBP("Great Britain pounds"),
    RUB("Russian rubles"),
    USD("United States");

    Currency(String description) {
        this.description = description;
    }

    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
