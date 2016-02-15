package com.mazebert.gateways.mysql;

import com.mazebert.entities.Player;
import com.mazebert.gateways.PlayerGateway;
import com.mazebert.gateways.error.GatewayError;
import com.mazebert.gateways.error.KeyAlreadyExists;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class MySqlPlayerGateway extends MySqlGateway implements PlayerGateway {
    private static final String FIND_PLAYER = "SELECT id, name, level, experience, lastUpdate, email, supporterLevel, relics, lastQuestCreation FROM Player";

    @Inject
    public MySqlPlayerGateway(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public Player addPlayer(Player player) {
        try {
            long id = getQueryRunner().insert("INSERT INTO Player (savekey, name, level, experience, lastUpdate, email, supporterLevel, relics, isCheater, lastQuestCreation) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?);", new ScalarHandler<>(),
                    player.getKey(),
                    player.getName(),
                    player.getLevel(),
                    player.getExperience(),
                    player.getLastUpdate(),
                    player.getEmail(),
                    player.getSupporterLevel(),
                    player.getRelics(),
                    player.isCheater(),
                    player.getLastQuestCreation());
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

    public Player findPlayerByKey(String key) {
        try {
            return getQueryRunner().query(FIND_PLAYER + " WHERE savekey=?;",
                    new BeanHandler<>(Player.class),
                    key);
        } catch (SQLException e) {
            throw new GatewayError("Failed to find player by key in database", e);
        }
    }

    @Override
    public Player findPlayerByEmail(String email) {
        try {
            return getQueryRunner().query(FIND_PLAYER + " WHERE LOWER(email)=LOWER(?);",
                    new BeanHandler<>(Player.class),
                    email);
        } catch (SQLException e) {
            throw new GatewayError("Failed to find player by email in database", e);
        }
    }

    @Override
    public int findPlayerRank(long id) {
        QueryRunner runner = getQueryRunner();
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
        try {
            getQueryRunner().update("UPDATE Player SET level=?, experience=?, lastUpdate=? WHERE id=?",
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
