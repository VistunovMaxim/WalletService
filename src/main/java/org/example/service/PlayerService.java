package org.example.service;

import org.example.entity.EntryActivity;
import org.example.entity.Player;
import org.example.entity.Transaction;
import org.example.util.dictionary.TransactionType;
import org.example.util.exception.AccessDeniedException;

import java.util.List;

public interface PlayerService {

    Player creatingNewPlayerAccount(String login, String password) throws AccessDeniedException;

    Player authorizationPlayerAccount(Player player, String password) throws AccessDeniedException;

    void exitPlayerAccount(Player player);

    List<Transaction> getPlayersTransactionHistory(Player player);

    List<EntryActivity> getPlayersActivityHistory(Player player);

    double getPlayerBalance(Player player);

    void doPlayersTransaction(Player player, TransactionType transactionType, String identifier,
                              double sumOfTransaction) throws AccessDeniedException;
}

