package org.example.service.impl;

import org.example.entity.Player;
import org.example.util.dictionary.TransactionType;
import org.example.service.PlayerService;
import org.example.util.exception.AccessDeniedException;
import org.springframework.stereotype.Component;

@Component
public class PlayerServiceImpl implements PlayerService {

    public Player creatingNewPlayerAccount(String login, String password) {
        return new Player(login, password);
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
    }
}
