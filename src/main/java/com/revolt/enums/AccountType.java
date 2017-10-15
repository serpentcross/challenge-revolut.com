package com.revolt.enums;

public enum AccountType {

    CHECKING("checking account"),
    DEPOSIT("deposit account"),
    LOAN("loan account");

    AccountType(String type) {
        this.type = type;
    }

    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
