package org.example.entity;

import java.util.ArrayList;
import java.util.List;

public class Player {

    private final String login;
    private final String password;
    private double balance;
    public final static List<String> transactionIdentifiers = new ArrayList<>();

    public Player(String login, String password) {
        this.login = login;
        this.password = password;
        this.balance = 0;
    }

    public Player(String login, String password, double balance) {
        this.login = login;
        this.password = password;
        this.balance = balance;
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

    public boolean validPass(String password) {
        return this.password.equals(password);
    }

    @Override
    public String toString() {
        return "Player Name - " + login + '\'' +
                ", actual balance = " + balance;
    }
}
