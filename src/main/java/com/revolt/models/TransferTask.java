package com.revolt.models;

import com.revolt.enums.TransactionResult;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

public class TransferTask implements Serializable {

    private static final long serialVersionUID = 1L;

    @Getter @Setter private int id;
    @Getter @Setter private Date transactionDate;

    @Getter @Setter private int receiverPhoneNumber;
    @Getter @Setter private int accountNumber;
    @Getter @Setter private double transferAmount;

    @Getter @Setter private TransactionResult transactionResult;


}
