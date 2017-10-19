package com.revolt.models;

import com.revolt.enums.TransactionResult;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

import java.util.Date;

public class TransferTask implements Serializable {

    private static final long serialVersionUID = 1L;

    public TransferTask(int id, Date transactionDate, int senderId, int accountId, double transferAmount, String receiverPhoneNumber, TransactionResult transactionResult) {
        this.id = id;
        this.transactionDate = transactionDate;
        this.senderId = senderId;
        this.accountId = accountId;
        this.transferAmount = transferAmount;
        this.receiverPhoneNumber = receiverPhoneNumber;
        this.transactionResult = transactionResult;
    }

    @Getter @Setter private int id;
    @Getter @Setter private Date transactionDate;
    @Getter @Setter private int senderId;
    @Getter @Setter private int accountId;
    @Getter @Setter private double transferAmount;
    @Getter @Setter private String receiverPhoneNumber;
    @Getter @Setter private TransactionResult transactionResult;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TransferTask that = (TransferTask) o;

        if (id != that.id) return false;
        if (senderId != that.senderId) return false;
        if (Double.compare(that.transferAmount, transferAmount) != 0) return false;
        return transactionDate.equals(that.transactionDate);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = id;
        result = 31 * result + transactionDate.hashCode();
        result = 31 * result + senderId;
        temp = Double.doubleToLongBits(transferAmount);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
