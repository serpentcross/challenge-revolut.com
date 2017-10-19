package com.revolt.enums;

public enum TransactionResult {

    SCS("Transaction has been successfuly completed."),
    PND("Transaction is in progress."),
    FRL("There were unknown errors during transaction"),
    SNF("Sender not found"),
    SNE("Sender account does not exist"),
    NEF("Not enough funds"),
    RNF("Receiver not found"),
    RNE("Receiver account does not exist");

    private String message;

    private TransactionResult(String message) {
        this.message = message;
    }

    @Override
    public String toString(){
        return message;
    }
}

