package org.example.repository.impl;

import org.example.entity.EntryActivity;
import org.example.entity.Player;
import org.example.entity.Transaction;
import org.example.repository.PlayersRepository;
import org.example.service.PlayerService;
import org.example.util.dictionary.TransactionType;
import org.example.util.exception.AccessDeniedException;
import org.example.util.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PlayersRepositoryImpl implements PlayersRepository {

    private final Map<String, Player> playerRepository = new HashMap<>();

    @Autowired
    private PlayerService playerService;
    private final Connection connection;

    private final String alreadyExists = "select login from players where login = ?";
    private final String createNewPlayer = "insert into players (login, password, balance) values (?, ?, ?)";

    @Autowired
    public PlayersRepositoryImpl(Connection connection) throws SQLException {
        String createTable = """
                 drop table if exists players;
                 create sequence my_table_id_seq;
                 create table if not exists players (
                   player_id integer default nextval('my_table_id_seq') primary key,
                   login varchar,
                   password varchar,
                   balance decimal
                );""";
        this.connection = connection;
        try (PreparedStatement preparedStatement = connection.prepareStatement(createTable)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Got SQL Exception - " + e.getMessage());
        }
    }

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
            try (PreparedStatement preparedStatement = connection.prepareStatement(createNewPlayer)) {
                preparedStatement.setString(1, login);
                preparedStatement.setString(2, password);
                preparedStatement.setDouble(3, player.getBalance());
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Got SQL Exception - " + e.getMessage());
            }
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
    } // ОБНОВЛЯТЬ ИНФУ В ТАБЛИЦЕ

    public boolean playerAlreadyExists(String login) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(alreadyExists)) {
            preparedStatement.setString(1, login);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Got SQL Exception - " + e.getMessage());
        }
        return false;
    }
}
