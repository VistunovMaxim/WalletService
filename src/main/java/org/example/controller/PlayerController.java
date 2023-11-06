package org.example.controller;

import org.example.entity.EntryActivity;
import org.example.entity.Player;
import org.example.entity.Transaction;
import org.example.util.dictionary.TransactionType;
import org.example.util.exception.AccessDeniedException;
import org.example.util.exception.UserNotFoundException;

import java.util.List;

public interface PlayerController {

    Player registration(String login, String password) throws AccessDeniedException;

    Player authorization(String login, String password) throws UserNotFoundException, AccessDeniedException;

    Object exit(Player player);

    double getBalance(Player player);

    void doTransaction(Player player, TransactionType transactionType, String identifier,
                       double sumOfTransaction) throws AccessDeniedException;

    List<Transaction> getTransactions(Player player);

    List<EntryActivity> getActions(Player player);
}
