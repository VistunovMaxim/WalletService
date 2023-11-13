package org.example.repository;

import org.example.entity.EntryActivity;
import org.example.entity.Player;
import org.example.entity.Transaction;
import org.example.util.dictionary.TransactionType;
import org.example.util.exception.AccessDeniedException;
import org.example.util.exception.UserNotFoundException;

import java.util.List;

public interface PlayersRepository {

    Player authorizationPlayerAccount(String login, String password) throws AccessDeniedException, UserNotFoundException;

    Player creatingNewPlayerAccount(String login, String password) throws AccessDeniedException;

    Object exitPlayerAccount(Player player);

    boolean playerAlreadyExists(String login);

    List<Transaction> getPlayersTransactionHistory(Player player);

    List<EntryActivity> getPlayersActivityHistory(Player player);

    double getPlayerBalance(Player player) throws AccessDeniedException;

    void doPlayersTransaction(Player player, TransactionType transactionType, String identifier,
                              double sumOfTransaction) throws AccessDeniedException;
}
