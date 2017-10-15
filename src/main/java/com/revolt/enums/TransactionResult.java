package com.revolt.enums;

public enum TransactionResult {

    SUCCESS("Transaction successful completed"),
    FAILRULE("There were errors during transaction");

    TransactionResult(String result) {
        this.result = result;
    }

    private String result;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }


}
