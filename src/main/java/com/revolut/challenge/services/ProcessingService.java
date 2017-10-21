package com.revolut.challenge.services;

import com.revolut.challenge.enums.TransactionResult;
import com.revolut.challenge.exceptions.PaymentException;
import com.revolut.challenge.models.Account;
import com.revolut.challenge.models.Customer;
import com.revolut.challenge.models.TransferTask;
import com.revolut.challenge.utils.CurrencyConverter;
import com.revolut.challenge.utils.FeedReader;

import javax.xml.transform.TransformerException;

import java.net.MalformedURLException;

import java.util.Date;
import java.util.Set;

public class ProcessingService {

    private Set<Customer> customers = FeedReader.getCustomers();

    private TransferTask transferTask;

    public String initiateTransfer(int senderId, int accountId, double amount, String receiverPhoneNumber) {

        transferTask = new TransferTask(new Date(), senderId, accountId, amount, receiverPhoneNumber, TransactionResult.PND);
        searchForSender(transferTask);
        return transferTask.toString();
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

        customers.add(commitTransaction(transferTask, customerSender, senderAccount, senderBallance));
        customers.add(commitTransaction(transferTask, customerReceiver, receiverAccount, Double.parseDouble(String.format("%.2f", receiverBallance))));

        FeedReader.setCustomers(customers);

        transferTask.setId(customerSender.getTransactionHistory().size());
        transferTask.setAccountId(senderAccount.getAccountNumber());
        transferTask.setCurrency(senderAccount.getCurrency());
        transferTask.setRemainedBallance(senderBallance);
        transferTask.setReceiverPhoneNumber(customerReceiver.getPhone());
        transferTask.setTransactionResult(TransactionResult.SCS);

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
