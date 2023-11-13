package org.example.mapper;

import org.example.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface PlayerMapper {

    Player resultSetToPlayer(ResultSet resultSet) throws SQLException;
}
