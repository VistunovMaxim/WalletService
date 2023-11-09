package org.example.service.impl;

import org.example.entity.EntryActivity;
import org.example.entity.Player;
import org.example.entity.Transaction;
import org.example.util.dictionary.ActionType;
import org.example.util.dictionary.TransactionType;
import org.example.service.PlayerService;
import org.example.util.exception.AccessDeniedException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class PlayerServiceImpl implements PlayerService {

    public Player creatingNewPlayerAccount(String login, String password) {
        Player player = new Player(login, password);
        EntryActivity createActivity = new EntryActivity(LocalDateTime.now(), ActionType.CREATED);
        EntryActivity entryActivity = new EntryActivity(LocalDateTime.now(), ActionType.ENTRY);
        player.getActionAudit().add(createActivity);
        player.getActionAudit().add(entryActivity);
        return player;
    }

    public Player authorizationPlayerAccount(Player player, String password) throws AccessDeniedException {
        if (player.validPass(password)) {
            EntryActivity entryActivity = new EntryActivity(LocalDateTime.now(), ActionType.ENTRY);
            player.getActionAudit().add(entryActivity);
            return player;
        } else {
            throw new AccessDeniedException("Неверный пароль");
        }
    }

    public void exitPlayerAccount(Player player) {
        EntryActivity entryActivity = new EntryActivity(LocalDateTime.now(), ActionType.EXIT);
        player.getActionAudit().add(entryActivity);
    }

    public List<Transaction> getPlayersTransactionHistory(Player player) {
        return player.getTransactions();
    }

    public List<EntryActivity> getPlayersActivityHistory(Player player) {
        return player.getActionAudit();
    }

    public double getPlayerBalance(Player player) {
        return player.getBalance();
    }

    public void doPlayersTransaction(Player player, TransactionType transactionType,
                                     String identifier, double sumOfTransaction) throws AccessDeniedException {
        if (Player.transactionIdentifiers.contains(identifier)) {
            throw new AccessDeniedException("Идентификатор транзакции не уникален");
        }
        Player.transactionIdentifiers.add(identifier);
        if (transactionType == TransactionType.DEBIT) {
            if (player.getBalance() < sumOfTransaction) {
                throw new AccessDeniedException("Недостаточно средств для совершения операции");
            }
            player.setBalance(player.getBalance() - sumOfTransaction);
        } else {
            player.setBalance(player.getBalance() + sumOfTransaction);
        }
        Transaction transaction = new Transaction(LocalDateTime.now(), sumOfTransaction, transactionType);
        player.getTransactions().add(transaction);
    }
}
