package org.example.repository.impl;

import org.example.entity.EntryActivity;
import org.example.entity.Player;
import org.example.entity.Transaction;
import org.example.repository.PlayersRepository;
import org.example.service.PlayerService;
import org.example.service.impl.PlayerServiceImpl;
import org.example.util.dictionary.TransactionType;
import org.example.util.exception.AccessDeniedException;
import org.example.util.exception.UserNotFoundException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayersRepositoryImpl implements PlayersRepository {

    private static final Map<String, Player> playerRepository = new HashMap<>();
    private static final PlayerService playerService = new PlayerServiceImpl();

    public Player authorizationPlayerAccount(String login, String password) throws AccessDeniedException, UserNotFoundException {
        if (playerAlreadyExists(login)) {
            return playerService.authorizationPlayerAccount(playerRepository.get(login), password);
        } else {
            throw new UserNotFoundException("Такой пользователь не найден");
        }
    }

    public Player creatingNewPlayerAccount(String login, String password) throws AccessDeniedException {
        if (playerAlreadyExists(login)) {
            throw new AccessDeniedException("Пользователь с таким именем уже существует");
        } else {
            Player player = playerService.creatingNewPlayerAccount(login, password);
            playerRepository.put(login, player);
            return player;
        }
    }

    public Object exitPlayerAccount(Player player) {
        playerService.exitPlayerAccount(player);
        return null;
    }

    public List<Transaction> getPlayersTransactionHistory(Player player) {
        return playerService.getPlayersTransactionHistory(player);
    }

    public List<EntryActivity> getPlayersActivityHistory(Player player) {
        return playerService.getPlayersActivityHistory(player);
    }

    public double getPlayerBalance(Player player) {
        return playerService.getPlayerBalance(player);
    }

    public void doPlayersTransaction(Player player, TransactionType transactionType, String identifier,
                                     double sumOfTransaction) throws AccessDeniedException {
        playerService.doPlayersTransaction(player, transactionType, identifier, sumOfTransaction);
    }

    public boolean playerAlreadyExists(String login) {
        return playerRepository.containsKey(login);
    }
}
