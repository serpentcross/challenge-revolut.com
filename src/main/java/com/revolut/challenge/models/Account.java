package com.revolut.challenge.models;

import com.revolut.challenge.enums.Currency;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

public class Account {

    public Account(int accountNumber, Currency currency, double balance) {
        this.accountNumber = accountNumber;
        this.currency = currency;
        this.balance = balance;
        this.creationDate = new Date();
    }

    @Getter @Setter private int accountNumber;
    @Getter @Setter private Currency currency;
    @Getter @Setter private Date creationDate;
    @Getter @Setter private double balance;
    @Getter @Setter private boolean isMain;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Account account = (Account) o;

        if (accountNumber != account.accountNumber) return false;
        if (currency != account.currency) return false;
        return creationDate.equals(account.creationDate);
    }

    @Override
    public int hashCode() {
        int result = accountNumber;
        result = 31 * result + currency.hashCode();
        result = 31 * result + creationDate.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return  "\n" +
                "account #" + accountNumber + "|" + "creation date: " + creationDate + "\n" +
                "......................................................................." + "\n" +
                "balance: " + balance + "\n" +
                "currency: " + currency + "\n" +
                "......................................................................." + "\n";
    }
}
