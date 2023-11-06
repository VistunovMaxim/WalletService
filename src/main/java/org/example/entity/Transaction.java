package org.example.entity;

import org.example.util.dictionary.TransactionType;
import java.time.LocalDateTime;

public class Transaction {
    double transactionAmount;
    TransactionType transactionType;
    LocalDateTime transactionDate;

    public Transaction(LocalDateTime transactionDate, double transactionAmount, TransactionType transactionType) {
        this.transactionDate = transactionDate;
        this.transactionAmount = transactionAmount;
        this.transactionType = transactionType;
    }

    @Override
    public String toString() {
        return "Транзакция - " + transactionType.getTitle() + "; Сумма - " + transactionAmount + "; Дата - " + transactionDate;
    }
}
