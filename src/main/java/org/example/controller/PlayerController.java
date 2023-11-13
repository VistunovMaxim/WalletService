package org.example.controller;

import org.example.entity.Player;
import org.example.util.dictionary.TransactionType;
import org.example.util.exception.AccessDeniedException;
import org.example.util.exception.UserNotFoundException;

import java.util.List;

public interface PlayerController {

    Player registration(String login, String password) throws AccessDeniedException;

    Player authorization(String login, String password) throws UserNotFoundException, AccessDeniedException;

    Object exit(Player player) throws AccessDeniedException;

    double getBalance(Player player) throws AccessDeniedException;

    void doTransaction(Player player, TransactionType transactionType, String identifier,
                       double sumOfTransaction) throws AccessDeniedException;

    List<String> getTransactions(Player player) throws AccessDeniedException;

    List<String> getActions(Player player) throws AccessDeniedException;
}
