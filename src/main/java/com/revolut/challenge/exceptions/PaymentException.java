package com.revolut.challenge.exceptions;

public class PaymentException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private String exceptionMessage;

    public PaymentException(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }

    @Override
    public String getMessage() {
        return "Transaction Failed! " + exceptionMessage;
    }

}
