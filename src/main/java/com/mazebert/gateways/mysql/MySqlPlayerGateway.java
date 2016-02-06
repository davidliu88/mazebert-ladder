package com.mazebert.gateways.mysql;

import com.mazebert.entities.Player;
import com.mazebert.gateways.PlayerGateway;
import com.mazebert.gateways.error.GatewayError;
import com.mazebert.gateways.error.KeyAlreadyExists;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class MySqlPlayerGateway implements PlayerGateway {
    private final DataSource dataSource;

    @Inject
    public MySqlPlayerGateway(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Player addPlayer(Player player) {
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            long id = runner.insert("INSERT INTO Player (savekey, name, level, experience, lastUpdate, email, supporterLevel, relics, isCheater) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?);", new ScalarHandler<>(),
                    player.getKey(),
                    player.getName(),
                    player.getLevel(),
                    player.getExperience(),
                    player.getLastUpdate(),
                    player.getEmail(),
                    player.getSupporterLevel(),
                    player.getRelics(),
                    player.isCheater());
            player.setId(id);
            return player;
        } catch (SQLException e) {
            if (e.getErrorCode() == MySqlErrorCode.DUPLICATE_ENTRY) {
                throw new KeyAlreadyExists();
            } else {
                throw new GatewayError("Failed to insert player into database.", e);
            }
        }
    }

    public Player findPlayer(String key) {
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            ResultSetHandler<Player> handler = new BeanHandler<>(Player.class);
            return runner.query("SELECT id, name, level, experience, lastUpdate, email, supporterLevel, relics FROM Player WHERE savekey=?;", handler, key);
        } catch (SQLException e) {
            throw new GatewayError("Failed to find player by key in database", e);
        }
    }

    @Override
    public int findPlayerRank(long id) {
        QueryRunner runner = new QueryRunner(dataSource);
        try(Connection connection = dataSource.getConnection()) {
            runner.update(connection, "SET @rownum := 0;");
            Long rank = runner.query(connection,
                    "SELECT rank FROM (" +
                        "SELECT @rownum := @rownum + 1 AS rank, id FROM Player WHERE isCheater=0 ORDER BY experience DESC, LOWER(name)" +
                    ") as result WHERE id=?;",
                    new ScalarHandler<>(), id);
            return rank == null ? 0 : rank.intValue();
        } catch (SQLException e) {
            return 0;
        }
    }

    public void updatePlayer(Player player) {
        QueryRunner runner = new QueryRunner(dataSource);
        try {
            runner.update("UPDATE Player SET level=?, experience=?, lastUpdate=? WHERE id=?",
                    player.getLevel(),
                    player.getExperience(),
                    player.getLastUpdate(),
                    player.getId()
            );
        } catch (SQLException e) {
            throw new GatewayError("Failed to update player in database", e);
        }
    }
}
