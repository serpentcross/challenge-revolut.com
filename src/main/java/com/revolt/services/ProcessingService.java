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

        customers.forEach(foundSender -> {
            if (foundSender.getId() == transferTask.getSenderId()) {
                searchForSenderAccount(transferTask, foundSender);
            }
        });

        throw new PaymentException(TransactionResult.SNF.toString());
    }

    private void searchForSenderAccount(TransferTask transferTask, Customer customerSender) {
        customerSender.getAccounts().forEach(foundSenderAccount -> {
            if (foundSenderAccount.getAccountNumber() == transferTask.getAccountId()) {
                checkAccountBallance(transferTask, customerSender, foundSenderAccount);
            }
        });

        throw new PaymentException(TransactionResult.SNE.toString());
    }

    private void checkAccountBallance(TransferTask transferTask, Customer customerSender, Account senderAccount) {
        if (senderAccount.getBalance() >= transferTask.getTransferAmount()) {
            searchForReceiver(transferTask, customerSender, senderAccount);
        }

        throw new PaymentException(TransactionResult.NEF.toString());
    }

    private void searchForReceiver(TransferTask transferTask, Customer customerSender, Account senderAccount) {
        customers.forEach(foundReceiver -> {
            if (foundReceiver.getPhone().equals(transferTask.getReceiverPhoneNumber())) {
                searchForReceiverAccount(transferTask, customerSender, senderAccount, foundReceiver);
            }
        });

        throw new PaymentException(TransactionResult.RNF.toString());
    }

    private void searchForReceiverAccount(TransferTask transferTask, Customer customerSender, Account senderAccount, Customer customerReceiver) {
        customerReceiver.getAccounts().forEach(foundReceiverAccount -> {
            if (foundReceiverAccount.isMain()) {
                transferFunds(transferTask, customerSender, senderAccount, customerReceiver, foundReceiverAccount);
            }
        });

        throw new PaymentException(TransactionResult.RNE.toString());
    }

    private void transferFunds(TransferTask transferTask, Customer customerSender, Account senderAccount, Customer customerReceiver, Account receiverAccount) {

        double transferAmount = transferTask.getTransferAmount();

        if (!senderAccount.getCurrency().equals(receiverAccount.getCurrency())) {
            try {
                transferAmount = CurrencyConverter.convertCurrency(transferTask.getTransferAmount(), senderAccount, receiverAccount);
            } catch (TransformerException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

        double senderBallance = senderAccount.getBalance() - transferTask.getTransferAmount();
        double receiverBallance = receiverAccount.getBalance() + transferAmount;

        transactionResult = TransactionResult.SCS.toString();
        transferTask.setTransactionResult(TransactionResult.SCS);

        customers.add(commitTransaction(transferTask, customerSender, senderAccount, senderBallance));
        customers.add(commitTransaction(transferTask, customerReceiver, receiverAccount, receiverBallance));

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
