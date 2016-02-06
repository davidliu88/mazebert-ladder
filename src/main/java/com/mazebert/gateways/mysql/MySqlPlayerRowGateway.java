package com.mazebert.gateways.mysql;

import com.mazebert.entities.PlayerRow;
import com.mazebert.error.Error;
import com.mazebert.error.Type;
import com.mazebert.gateways.PlayerRowGateway;
import com.mazebert.gateways.error.GatewayError;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class MySqlPlayerRowGateway implements PlayerRowGateway {
    private final DataSource dataSource;

    @Inject
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
            throw new GatewayError("Failed to select player rows from database", e);
        }
    }

    @Override
    public List<PlayerRow> findPlayersUpdatedSince(Date updatedSince) {
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            return runner.query("SELECT id, name FROM Player WHERE lastUpdate >= ? ORDER BY LOWER(name) ASC;",
                    new BeanListHandler<>(PlayerRow.class),
                    updatedSince);
        } catch (SQLException e) {
            throw new GatewayError("Failed to select players updated since from database", e);
        }
    }

    @Override
    public int getTotalPlayerCount() {
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            Long count = runner.query("SELECT COUNT(*) FROM Player;", new ScalarHandler<>());
            return count == null ? 0 : count.intValue();
        } catch (SQLException e) {
            throw new GatewayError("Failed to select amount of players from database", e);
        }
    }
}
