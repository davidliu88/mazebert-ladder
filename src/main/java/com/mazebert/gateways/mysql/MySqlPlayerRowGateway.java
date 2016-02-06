package com.mazebert.gateways.mysql;

import com.mazebert.entities.PlayerRow;
import com.mazebert.error.Error;
import com.mazebert.error.Type;
import com.mazebert.gateways.PlayerRowGateway;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class MySqlPlayerRowGateway implements PlayerRowGateway {
    private final DataSource dataSource;

    public MySqlPlayerRowGateway(DataSource dataSource) {

        this.dataSource = dataSource;
    }

    @Override
    public List<PlayerRow> findPlayers(int start, int limit) {
        QueryRunner runner = new QueryRunner(dataSource);
        try(Connection connection = dataSource.getConnection()) {
            runner.update(connection, "SET @rownum := ?;", start);
            return runner.query(connection,
                    "SELECT @rownum := @rownum + 1 AS rank, id, name, level, experience FROM Player " +
                    "ORDER BY experience DESC, LOWER(name) ASC " +
                    "LIMIT ?, ?;",
                    new BeanListHandler<>(PlayerRow.class), start, limit);
        } catch (SQLException e) {
            throw new Error(Type.INTERNAL_SERVER_ERROR, "Failed to select player rows from database", e);
        }
    }

    @Override
    public List<PlayerRow> findPlayersNowPlaying(int toleranceInMinutes) {
        return null;
    }

    @Override
    public int getTotalPlayerCount() {
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            Integer count = runner.query("SELECT * FROM Player;", new ScalarHandler<>());
            return count == null ? 0 : count;
        } catch (SQLException e) {
            throw new Error(Type.INTERNAL_SERVER_ERROR, "Failed to select amount of players from database", e);
        }
    }
}
