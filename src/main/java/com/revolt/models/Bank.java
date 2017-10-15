package com.revolt.models;

import com.revolt.utils.DateConverter;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

public class Bank {

    public Bank(int id, String name, String inn, Date foundationDate) {
        this.id = id;
        this.name = name;
        this.inn = inn;
        this.foundationDate = foundationDate;
    }

    public Bank(int id, String name, String inn, Date foundationDate, Set<Customer> customers) {
        this.id = id;
        this.name = name;
        this.inn = inn;
        this.foundationDate = foundationDate;
        this.customers = customers;
    }

    @Getter @Setter private int id;
    @Getter @Setter private String name;
    @Getter @Setter private String inn;
    @Getter @Setter private Date foundationDate;
    @Getter @Setter private Set<Customer> customers;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Bank bank = (Bank) o;

        if (id != bank.id) return false;
        if (!name.equals(bank.name)) return false;
        if (!inn.equals(bank.inn)) return false;
        return foundationDate.equals(bank.foundationDate);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + name.hashCode();
        result = 31 * result + inn.hashCode();
        result = 31 * result + foundationDate.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "<p><strong>bank # " + getId() + "</strong></p>\n" +
                "<p><em>name</em> : " + getName() + ";&nbsp;</p>\n" +
                "<p><em>was founded</em> : " + DateConverter.displayDate(getFoundationDate()) + ";</p>\n" +
                "<p><em>inn</em> : " + getInn() + ";</p>\n" +
                "<p>customers : " + getCustomers().size() + ";</p>\n" +
                "<p>active accounts : " +countActiveAccounts() + "</p>";
    }

    private int countActiveAccounts() {

        int activeAccounts = 0;

        for (Customer customer : getCustomers()) {
            for (Account account : customer.getAccounts()) {
                if (account.isActive()) {
                    activeAccounts++;
                }
            }
        }

        return activeAccounts;
    }
}
