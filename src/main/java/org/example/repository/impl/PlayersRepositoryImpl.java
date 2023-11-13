package org.example.repository.impl;

import org.example.entity.EntryActivity;
import org.example.entity.Player;
import org.example.entity.Transaction;
import org.example.mapper.PlayerMapper;
import org.example.repository.PlayersRepository;
import org.example.service.PlayerService;
import org.example.util.dictionary.ActionType;
import org.example.util.dictionary.TransactionType;
import org.example.util.exception.AccessDeniedException;
import org.example.util.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class PlayersRepositoryImpl implements PlayersRepository {

    @Autowired
    private PlayerService playerService;
    @Autowired
    private PlayerMapper playerMapper;
    @Autowired
    private final Connection connection;

    @Autowired
    public PlayersRepositoryImpl(Connection connection) {
        String createPlayersTable = """
                 create sequence if not exists activity_id_seq;
                 create table if not exists players (
                   player_id integer default nextval('my_table_id_seq') primary key,
                   login varchar not null unique,
                   password varchar,
                   balance decimal
                );""";
        String createPlayersActivityTable = """
                create sequence if not exists activity_id_seq;
                create table if not exists activity_table (
                    action_id integer default nextval('activity_id_seq') primary key,
                    login varchar not null references players(login),
                    action varchar
                );""";
        String createPlayersTransactionTable = """
                create sequence if not exists transaction_id_seq;
                create table if not exists transactions_table (
                    transaction_id integer default nextval('transaction_id_seq') primary key,
                    login varchar not null references players(login),
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
            String addAuthorizationAction = "insert into activity_table (login, action) values (?, ?)";
            EntryActivity entryActivity = new EntryActivity(LocalDateTime.now(), ActionType.ENTRY);
            try (PreparedStatement preparedStatement = connection.prepareStatement(getPlayerAccount);
                 PreparedStatement pSActivity = connection.prepareStatement(addAuthorizationAction)) {
                preparedStatement.setString(1, login);
                preparedStatement.setString(2, password);
                pSActivity.setString(1, login);
                pSActivity.setString(2, entryActivity.toString());
                pSActivity.executeUpdate();
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
            String addACreationAction = "insert into activity_table (login, action) values (?, ?)";
            String addAuthorizationAction = "insert into activity_table (login, action) values (?, ?)";
            EntryActivity entryActivity = new EntryActivity(LocalDateTime.now(), ActionType.ENTRY);
            EntryActivity createActivity = new EntryActivity(LocalDateTime.now(), ActionType.CREATED);
            try (PreparedStatement preparedStatement = connection.prepareStatement(createNewPlayer);
                 PreparedStatement pSCreate = connection.prepareStatement(addACreationAction);
                 PreparedStatement pSAuthorization = connection.prepareStatement(addAuthorizationAction)) {
                preparedStatement.setString(1, login);
                preparedStatement.setString(2, password);
                preparedStatement.setDouble(3, player.getBalance());
                preparedStatement.executeUpdate();
                pSCreate.setString(1, login);
                pSCreate.setString(2, createActivity.toString());
                pSAuthorization.setString(1, login);
                pSAuthorization.setString(2, entryActivity.toString());
                pSCreate.executeUpdate();
                pSAuthorization.executeUpdate();
            } catch (SQLException e) {
                throw new AccessDeniedException("Got SQL Exception - " + e.getMessage());
            }
            return player;
        }
    }

    public Object exitPlayerAccount(Player player) throws AccessDeniedException {
        String addAExitAction = "insert into activity_table (login, action) values (?, ?)";
        EntryActivity exitActivity = new EntryActivity(LocalDateTime.now(), ActionType.EXIT);
        try (PreparedStatement pSExit = connection.prepareStatement(addAExitAction)) {
            pSExit.setString(1, player.getLogin());
            pSExit.setString(2, exitActivity.toString());
            pSExit.executeUpdate();
        } catch (SQLException e) {
            throw new AccessDeniedException("Got SQL Exception - " + e.getMessage());
        }
        return null;
    }

    public List<String> getPlayersTransactionHistory(Player player) throws AccessDeniedException {
        String getTransactionHistory = "select transaction from transactions_table where login = ?";
        List<String> transactionHistory = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(getTransactionHistory)) {
            preparedStatement.setString(1, player.getLogin());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                transactionHistory.add(resultSet.getString(1));
            }
        } catch (SQLException e) {
            throw new AccessDeniedException("Got SQL Exception - " + e.getMessage());
        }
        return transactionHistory;
    }

    public List<String> getPlayersActivityHistory(Player player) throws AccessDeniedException {
        String getActionHistory = "select action from activity_table where login = ?";
        List<String> actionHistory = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(getActionHistory)) {
            preparedStatement.setString(1, player.getLogin());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                actionHistory.add(resultSet.getString(1));
            }
        } catch (SQLException e) {
            throw new AccessDeniedException("Got SQL Exception - " + e.getMessage());
        }
        return actionHistory;
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
        String addTransactionHistory = "insert into transactions_table (login, transaction) values (?, ?)";
        Transaction transaction = new Transaction(LocalDateTime.now(), sumOfTransaction, transactionType);
        try (PreparedStatement preparedStatement = connection.prepareStatement(doTransaction);
             PreparedStatement pSAddTransaction = connection.prepareStatement(addTransactionHistory)) {
            preparedStatement.setDouble(1, player.getBalance());
            preparedStatement.setString(2, player.getLogin());
            pSAddTransaction.setString(1, player.getLogin());
            pSAddTransaction.setString(2, transaction.toString());
            preparedStatement.executeUpdate();
            pSAddTransaction.executeUpdate();
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