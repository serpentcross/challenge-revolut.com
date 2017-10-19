package com.revolt.utils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import com.revolt.enums.Currency;

import com.revolt.models.Account;
import com.revolt.models.Customer;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class FeedReader {

    private static Set<Customer> customers = new HashSet<>();

    public static void parseFoundJSONfeed(File singleJSONFile) {

        JSONParser parser = new JSONParser();
        JSONArray jsonFeed = null;

        try {
            jsonFeed = (JSONArray) parser.parse(new FileReader(singleJSONFile.getAbsolutePath()));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (jsonFeed == null || jsonFeed.size() == 0) {
            getEmptyCustomers();
        } else {

            int customerId = 0;
            int accountNumber = 0;

            for (Object singleCustomer : jsonFeed) {

                ++customerId;

                Set<Account> accounts = new HashSet<>();

                JSONObject customersJSON = (JSONObject) singleCustomer;

                String name = null;
                String surname = null;
                String phone = null;
                Date birthDate = null;

                try {

                    name = (String) customersJSON.get("name");
                    surname = (String) customersJSON.get("surname");
                    phone = (String) customersJSON.get("phone");
                    birthDate = DateConverter.formatter.parse((String) customersJSON.get("birthDate"));

                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                }

                JSONArray accountsArray = (JSONArray) customersJSON.get("accounts");

                for (Object singleAccount : accountsArray) {

                    ++accountNumber;

                    JSONObject accountsJSON = (JSONObject) singleAccount;

                    Currency currency = Currency.valueOf((String) accountsJSON.get("currency"));
                    int ballance = Integer.parseInt(accountsJSON.get("ballance").toString());

                    accounts.add(new Account(accountNumber, currency, ballance));
                }

                setRandomMainAccount(accounts);

                customers.add(new Customer(customerId, name, surname, phone, birthDate, accounts));
            }
        }
    }

    private static Set<Account> setRandomMainAccount(Set<Account> accounts) {

        Iterator<Account> accountIterator = accounts.iterator();

        if (accounts.size() == 1) {

            for (accounts.iterator(); accountIterator.hasNext();) {
                accountIterator.next().setMain(true);
            }
        } else {
            Random random = new Random();
            int randomAccount = random.nextInt(accounts.size());
            int counter = 0;
            for(Account account : accounts) {
                if (counter == randomAccount) {
                    account.setMain(true);
                }
                counter++;
            }
        }

        return accounts;
    }

    public static Set<Customer> getCustomers() {
        return customers;
    }

    public static void setCustomers(Set<Customer> customers) {
        FeedReader.customers = customers;
    }

    private static void getEmptyCustomers() {
        customers = new HashSet<>();
    }
}
