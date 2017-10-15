package com.revolt.utils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.revolt.enums.AccountType;
import com.revolt.enums.Currency;
import com.revolt.models.Account;
import com.revolt.models.Bank;
import com.revolt.models.Customer;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class FeedReader {

    private static Set<Bank> banks = new HashSet<>();
    private static Set<Customer> customers = new HashSet<>();

    private static final SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

    public static void readFilesFromFolder(File folder) {
        for (File fileEntry : folder.listFiles()) {
            if(!fileEntry.isDirectory()) {
                parseFoundJSONfeed(fileEntry);
            }
        }
    }

    private static void parseFoundJSONfeed(File singleJSONFile) {

        JSONParser parser = new JSONParser();
        JSONArray jsonFeed = null;

        try {
            jsonFeed = (JSONArray) parser.parse(new FileReader(singleJSONFile.getAbsolutePath()));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        switch (singleJSONFile.getName()) {

            case "banks.json":

                int bankId = 0;

                for (Object singleObject: jsonFeed) {

                    ++bankId;

                    JSONObject banksJSON = (JSONObject) singleObject;

                    String name = null;
                    String inn = null;
                    Date foundationDate = null;

                    try {
                        name = (String) banksJSON.get("name");
                        inn = (String) banksJSON.get("inn");
                        foundationDate = formatter.parse((String) banksJSON.get("foundationDate"));
                    } catch (java.text.ParseException e) {
                        e.printStackTrace();
                    }
                    banks.add(new Bank(bankId, name, inn, foundationDate));
                }

            break;

            case "customers.json":

               int customerId = 0;

                    for (Object singleCustomer: jsonFeed) {

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
                            birthDate = formatter.parse((String) customersJSON.get("birthDate"));

                        } catch (java.text.ParseException e) {
                            e.printStackTrace();
                        }

                        JSONArray accountsArray = (JSONArray) customersJSON.get("accounts");

                        for (Object singleAccount : accountsArray) {

                            JSONObject accountsJSON = (JSONObject) singleAccount;

                            Currency currency = Currency.valueOf((String) accountsJSON.get("currency"));
                            int ballance = Integer.parseInt(accountsJSON.get("ballance").toString());

                            accounts.add(new Account(currency, ballance));
                        }
                        customers.add(new Customer(customerId, name, surname, phone, birthDate, accounts));
                    }
            break;
        }

        for (Bank singleBank: banks) {
            singleBank.setCustomers(customers);
        }

    }

    public static Set<Bank> getBanks() {
        return banks;
    }
}
