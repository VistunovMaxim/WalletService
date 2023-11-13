package org.example.entity;

import java.util.ArrayList;
import java.util.List;

public class Player {

    private final String login;
    private final String password;
    private double balance;
    private final List<EntryActivity> actionAudit;
    private final List<Transaction> transactions;
    public final static List<String> transactionIdentifiers = new ArrayList<>();

    public Player(String login, String password) {
        this.login = login;
        this.password = password;
        this.balance = 0;
        this.actionAudit = new ArrayList<>();
        this.transactions = new ArrayList<>();
    }

    public Player(String login, String password, double balance, List<EntryActivity> actionAudit, List<Transaction> transactions) {
        this.login = login;
        this.password = password;
        this.balance = balance;
        this.actionAudit = actionAudit;
        this.transactions = transactions;
    }

    public String getLogin() {
        return login;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getBalance() {
        return balance;
    }

    public List<EntryActivity> getActionAudit() {
        return actionAudit;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public boolean validPass(String password) {
        return this.password.equals(password);
    }

    @Override
    public String toString() {
        return "Player Name - " + login + '\'' +
                ", actual balance = " + balance;
    }
}
