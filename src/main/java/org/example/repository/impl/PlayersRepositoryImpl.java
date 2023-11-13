package org.example.repository.impl;

import org.example.entity.EntryActivity;
import org.example.entity.Player;
import org.example.entity.Transaction;
import org.example.mapper.PlayerMapper;
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
import java.util.List;

@Component
public class PlayersRepositoryImpl implements PlayersRepository {

    @Autowired
    private PlayerService playerService;
    @Autowired
    private PlayerMapper playerMapper;
    private final Connection connection;

    @Autowired
    public PlayersRepositoryImpl(Connection connection) {
        String createPlayersTable = """
                 create table if not exists players (
                   player_id integer default nextval('my_table_id_seq') primary key,
                   login varchar,
                   password varchar,
                   balance decimal
                );""";
        String createPlayersActivityTable = """
                drop table if exists activity_table;
                drop sequence if exists activity_id_seq;
                create sequence activity_id_seq;
                create table activity_table (
                    action_id integer default nextval('activity_id_seq') primary key,
                    player_id integer not null references players(player_id),
                    action varchar
                );""";
        String createPlayersTransactionTable = """
                drop table if exists transactions_table;
                drop sequence if exists transaction_id_seq;
                create sequence transaction_id_seq;
                create table transactions_table (
                    transaction_id integer default nextval('transaction_id_seq') primary key,
                    player_id integer not null references players(player_id),
                    transaction varchar
                );""";

        this.connection = connection;

        try (PreparedStatement psCreatePlayersTab = connection.prepareStatement(createPlayersTable);
             PreparedStatement psCreateActivityTab = connection.prepareStatement(createPlayersActivityTable);
             PreparedStatement psCreateTransactionTab = connection.prepareStatement(createPlayersTransactionTable)) {
            psCreatePlayersTab.executeUpdate();
            psCreateActivityTab.executeUpdate();
            psCreateTransactionTab.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Got SQL Exception - " + e.getMessage());
        }
    }

    public Player authorizationPlayerAccount(String login, String password) throws AccessDeniedException, UserNotFoundException {
        if (playerAlreadyExists(login)) {
            String getPlayerAccount = "select * from players where login = ? and password = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(getPlayerAccount)) {
                preparedStatement.setString(1, login);
                preparedStatement.setString(2, password);
                ResultSet resultSet = preparedStatement.executeQuery();
                return playerMapper.resultSetToPlayer(resultSet);
            } catch (SQLException e) {
                throw new AccessDeniedException("Got SQL Exception - " + e.getMessage());
            }
        } else {
            throw new UserNotFoundException("Такой пользователь не найден");
        }
    }

    public Player creatingNewPlayerAccount(String login, String password) throws AccessDeniedException {
        if (playerAlreadyExists(login)) {
            throw new AccessDeniedException("Пользователь с таким именем уже существует");
        } else {
            Player player = playerService.creatingNewPlayerAccount(login, password);
            String createNewPlayer = "insert into players (login, password, balance) values (?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(createNewPlayer)) {
                preparedStatement.setString(1, login);
                preparedStatement.setString(2, password);
                preparedStatement.setDouble(3, player.getBalance());
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new AccessDeniedException("Got SQL Exception - " + e.getMessage());
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

    public double getPlayerBalance(Player player) throws AccessDeniedException {
        String getBalance = "select balance from players where login = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(getBalance)) {
            preparedStatement.setString(1, player.getLogin());
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getDouble(1);
        } catch (SQLException e) {
            throw new AccessDeniedException("Got SQL Exception - " + e.getMessage());
        }
    }

    public void doPlayersTransaction(Player player, TransactionType transactionType, String identifier,
                                     double sumOfTransaction) throws AccessDeniedException {
        playerService.doPlayersTransaction(player, transactionType, identifier, sumOfTransaction);
        String doTransaction = "update players set balance = ? where login = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(doTransaction)) {
            preparedStatement.setDouble(1, player.getBalance());
            preparedStatement.setString(2, player.getLogin());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Got SQL Exception - " + e.getMessage());
        }
    }

    public boolean playerAlreadyExists(String login) {
        String alreadyExists = "select login from players where login = ?";
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


//                 drop table if exists players cascade;
//                 drop sequence if exists my_table_id_seq;
//                 create sequence my_table_id_seq;