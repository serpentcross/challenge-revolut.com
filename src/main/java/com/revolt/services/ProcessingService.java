package com.revolt.services;

import com.revolt.enums.TransactionResult;

import com.revolt.exceptions.PaymentException;
import com.revolt.models.Account;
import com.revolt.models.Customer;
import com.revolt.models.TransferTask;

import com.revolt.utils.CurrencyConverter;
import com.revolt.utils.FeedReader;

import javax.xml.transform.TransformerException;

import java.net.MalformedURLException;
import java.util.Date;
import java.util.Set;

public class ProcessingService {

    private Set<Customer> customers = FeedReader.getCustomers();
    private String transactionResult = TransactionResult.PND.toString();

    public String initiateTransfer(int senderId, int accountId, double amount, String receiverPhoneNumber) {

        int id = 0;

        TransferTask transferTask = new TransferTask(++id, new Date(), senderId, accountId, amount, receiverPhoneNumber, TransactionResult.PND);

        searchForSender(transferTask);

        return transactionResult;
    }

    private void searchForSender(TransferTask transferTask) {
        Customer foundSender = customers.stream()
                .filter(sender -> transferTask.getSenderId() == sender.getId())
                .findFirst()
                .orElseThrow(() -> new PaymentException(TransactionResult.SNF.toString()));

        searchForSenderAccount(transferTask, foundSender);
    }

    private void searchForSenderAccount(TransferTask transferTask, Customer customerSender) {
        Account foundSenderAccount = customerSender.getAccounts().stream()
                .filter(sAccount -> transferTask.getAccountId() == sAccount.getAccountNumber())
                .findFirst()
                .orElseThrow(() -> new PaymentException(TransactionResult.SNE.toString()));

        checkAccountBallance(transferTask, customerSender, foundSenderAccount);
    }

    private void checkAccountBallance(TransferTask transferTask, Customer customerSender, Account senderAccount) {
        if (senderAccount.getBalance() >= transferTask.getTransferAmount()) {
            searchForReceiver(transferTask, customerSender, senderAccount);
        } else {
            throw new PaymentException(TransactionResult.NEF.toString());
        }
    }

    private void searchForReceiver(TransferTask transferTask, Customer customerSender, Account senderAccount) {
        Customer foundReceiver = customers.stream()
                .filter(receiver -> transferTask.getReceiverPhoneNumber().equals(receiver.getPhone()))
                .findFirst()
                .orElseThrow(() -> new PaymentException(TransactionResult.RNF.toString()));

        searchForReceiverAccount(transferTask, customerSender, senderAccount, foundReceiver);
    }

    private void searchForReceiverAccount(TransferTask transferTask, Customer customerSender, Account senderAccount, Customer customerReceiver) {

        Account foundReceiverAccount = customerReceiver.getAccounts().stream()
                .filter(Account::isMain)
                .findFirst()
                .orElseThrow(() -> new PaymentException(TransactionResult.RNE.toString()));

        transferFunds(transferTask, customerSender, senderAccount, customerReceiver, foundReceiverAccount);
    }

    private void transferFunds(TransferTask transferTask, Customer customerSender, Account senderAccount, Customer customerReceiver, Account receiverAccount) {

        double transferAmount = transferTask.getTransferAmount();

        if (!senderAccount.getCurrency().equals(receiverAccount.getCurrency())) {
            try {
                transferAmount = CurrencyConverter.convertCurrency(transferTask.getTransferAmount(), senderAccount, receiverAccount);
            } catch (MalformedURLException ex) {
                ex.printStackTrace();
            } catch (TransformerException e) {
                e.printStackTrace();
            }
        }

        double senderBallance = senderAccount.getBalance() - transferTask.getTransferAmount();
        double receiverBallance = receiverAccount.getBalance() + transferAmount;

        transactionResult = TransactionResult.SCS.toString();
        transferTask.setTransactionResult(TransactionResult.SCS);

        customers.add(commitTransaction(transferTask, customerSender, senderAccount, senderBallance));
        customers.add(commitTransaction(transferTask, customerReceiver, receiverAccount, Double.parseDouble(String.format("%.2f", receiverBallance))));

        FeedReader.setCustomers(customers);

    }

    private Customer commitTransaction(TransferTask transferTask, Customer customer, Account account, double balance) {
        customer.getAccounts().forEach(foundAccount -> {
            if ( foundAccount.equals(account) ) {
                foundAccount.setBalance(balance);
                customer.getAccounts().add(foundAccount);
                customer.getTransactionHistory().add(transferTask);
            }
        });
        return customer;
    }
}
