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
            long id = runner.insert("INSERT INTO Player (savekey, name, level, experience, lastUpdate) VALUES(?, ?, ?, ?, ?);", new ScalarHandler<>(),
                    player.getKey(),
                    player.getName(),
                    player.getLevel(),
                    player.getExperience(),
                    player.getLastUpdate());
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
            return runner.query("SELECT id, name, level, experience FROM Player WHERE savekey=?;", handler, key);
        } catch (SQLException e) {
            throw new GatewayError("Failed to find player by key in database", e);
        }
    }

    public void updatePlayer(Player player) {

    }
}
