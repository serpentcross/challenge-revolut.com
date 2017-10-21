package com.revolut.challenge.models;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;

public class Customer {

    public Customer(int id, String name, String surname, String phone, Date birthDate, Set<Account> accounts)  {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.birthDate = birthDate;
        this.accounts = accounts;
        this.transactionHistory = new ArrayList<>();
    }

    @Getter @Setter private int id;
    @Getter @Setter private String name;
    @Getter @Setter private String surname;
    @Getter @Setter private String phone;
    @Getter @Setter private Date birthDate;
    @Getter @Setter private Set<Account> accounts;
    @Getter @Setter private ArrayList<TransferTask> transactionHistory;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Customer customer = (Customer) o;

        if (id != customer.id) return false;
        if (!name.equals(customer.name)) return false;
        return birthDate.equals(customer.birthDate);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + name.hashCode();
        result = 31 * result + birthDate.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Customer # " + id + "\n" +
                "-----------------------------------------------------------------------" + "\n" +
                "initials: " + name + " " + surname + "\n" +
                "birth date: " + birthDate + "\n" +
                "phone: " + phone + "\n" +
                "-----------------------------------------------------------------------" + "\n" +
                "accounts info: " + "\n" +
                "+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++" + "\n" +
                printAccounts() + "\n" +
                "########################################################################" + "\n" +
                "#                                                                      #" + "\n" +
                "#                         TRANSACTION HISTORY                          #" + "\n" +
                "#                                                                      #" + "\n" +
                "########################################################################" + "\n" +
                transactionHistory() + "\n";
    }

    private String printAccounts() {

        String accInfo = "";

        if (accounts.size() > 0) {
            for (Account acc : accounts) {
                accInfo += acc.toString();
            }
        } else {
            accInfo = "No accounts found.";
        }

        return accInfo;
    }

    private String transactionHistory() {

        String transactionInfo = "";

        if (transactionHistory.size() > 0) {
            for (TransferTask trt : transactionHistory) {
                transactionInfo += trt.toString();
            }
        } else {
            transactionInfo = "Transaction history is empty.";
        }

        return transactionInfo;
    }
}
