package com.revolut.challenge.enums;

public enum Currency {

    GBP("Great Britain Pounds"),
    RUB("Russian rubles"),
    USD("United States Dollars");

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
