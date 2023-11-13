package org.example.repository;

import org.example.entity.Player;
import org.example.util.dictionary.TransactionType;
import org.example.util.exception.AccessDeniedException;
import org.example.util.exception.UserNotFoundException;

import java.util.List;

public interface PlayersRepository {

    Player authorizationPlayerAccount(String login, String password) throws AccessDeniedException, UserNotFoundException;

    Player creatingNewPlayerAccount(String login, String password) throws AccessDeniedException;

    Object exitPlayerAccount(Player player) throws AccessDeniedException;

    boolean playerAlreadyExists(String login);

    List<String> getPlayersTransactionHistory(Player player) throws AccessDeniedException;

    List<String> getPlayersActivityHistory(Player player) throws AccessDeniedException;

    double getPlayerBalance(Player player) throws AccessDeniedException;

    void doPlayersTransaction(Player player, TransactionType transactionType, String identifier,
                              double sumOfTransaction) throws AccessDeniedException;
}
