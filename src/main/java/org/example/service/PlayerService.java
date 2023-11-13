package org.example.service;

import org.example.entity.Player;
import org.example.util.dictionary.TransactionType;
import org.example.util.exception.AccessDeniedException;

public interface PlayerService {

    Player creatingNewPlayerAccount(String login, String password) throws AccessDeniedException;

    void doPlayersTransaction(Player player, TransactionType transactionType, String identifier,
                              double sumOfTransaction) throws AccessDeniedException;
}

