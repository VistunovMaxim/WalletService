package org.example.controller.impl;

import org.example.controller.PlayerController;
import org.example.entity.EntryActivity;
import org.example.entity.Player;
import org.example.entity.Transaction;
import org.example.repository.PlayersRepository;
import org.example.util.dictionary.TransactionType;
import org.example.util.exception.AccessDeniedException;
import org.example.util.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PlayerControllerImpl implements PlayerController {
    @Autowired
    private PlayersRepository playersRepository;

    public Player registration(String login, String password) throws AccessDeniedException {
        return playersRepository.creatingNewPlayerAccount(login, password);
    }

    public Player authorization(String login, String password) throws UserNotFoundException, AccessDeniedException {
        return playersRepository.authorizationPlayerAccount(login, password);
    }

    public Object exit(Player player) {
        return playersRepository.exitPlayerAccount(player);
    }

    public double getBalance(Player player) {
        return playersRepository.getPlayerBalance(player);
    }

    public void doTransaction(Player player, TransactionType transactionType, String identifier, double sumOfTransaction) throws AccessDeniedException {
        playersRepository.doPlayersTransaction(player, transactionType, identifier, sumOfTransaction);
    }

    public List<Transaction> getTransactions(Player player) {
        return playersRepository.getPlayersTransactionHistory(player);
    }

    public List<EntryActivity> getActions(Player player) {
        return playersRepository.getPlayersActivityHistory(player);
    }
}
