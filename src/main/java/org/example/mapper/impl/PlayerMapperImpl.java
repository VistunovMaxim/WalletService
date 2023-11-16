package org.example.mapper.impl;

import org.example.entity.Player;
import org.example.mapper.PlayerMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class PlayerMapperImpl implements PlayerMapper {

    @Override
    public Player resultSetToPlayer(ResultSet resultSet) throws SQLException {
        String login = resultSet.getString(2);
        String password = resultSet.getString(3);
        double balance = resultSet.getDouble(4);

        return new Player(login, password, balance);
    }
}
