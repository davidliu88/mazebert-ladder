package com.mazebert.gateways.mysql;

import com.google.common.collect.Lists;
import com.mazebert.entities.Player;
import com.mazebert.entities.Quest;
import com.mazebert.gateways.QuestGateway;
import com.mazebert.gateways.error.GatewayError;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class MySqlQuestGateway implements QuestGateway {
    private final DataSource dataSource;

    @Inject
    public MySqlQuestGateway(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<Long> findCompletedHiddenQuestIds(long playerId) {
        QueryRunner queryRunner = new QueryRunner(dataSource);
        try {
            List<Integer> ids = queryRunner.query("SELECT questId FROM PlayerHiddenQuest WHERE playerId=?;",
                    new ColumnListHandler<>(1),
                    playerId);
            return Lists.transform(ids, Integer::longValue);
        } catch (SQLException e) {
            throw new GatewayError("Failed to find completed hidden quest ids for player.", e);
        }
    }

    @Override
    public List<Quest> findDailyQuests(long playerId) {
        QueryRunner queryRunner = new QueryRunner(dataSource);
        try {
            return queryRunner.query("SELECT questId AS id, reward, isHidden, sinceVersion, requiredAmount FROM PlayerDailyQuest, Quest WHERE playerId=? AND Quest.id=PlayerDailyQuest.questId ORDER BY creationDate ASC;",
                    new BeanListHandler<>(Quest.class),
                    playerId);
        } catch (SQLException e) {
            throw new GatewayError("Failed to find daily quests for player.", e);
        }
    }

    @Override
    public List<Long> findDailyQuestIds(long playerId) {
        QueryRunner queryRunner = new QueryRunner(dataSource);
        try {
            List<Integer> ids = queryRunner.query("SELECT questId FROM PlayerDailyQuest WHERE playerId=? ORDER BY creationDate ASC;",
                    new ColumnListHandler<>(1),
                    playerId);
            return Lists.transform(ids, Integer::longValue);
        } catch (SQLException e) {
            throw new GatewayError("Failed to determine daily quest ids for player.", e);
        }
    }

    @Override
    public List<Quest> findAllQuests() {
        QueryRunner queryRunner = new QueryRunner(dataSource);
        try {
            return queryRunner.query("SELECT id, reward, isHidden, sinceVersion, requiredAmount FROM Quest;",
                    new BeanListHandler<>(Quest.class));
        } catch (SQLException e) {
            throw new GatewayError("Failed to determine all available quests.", e);
        }
    }

    @Override
    public void addDailyQuest(Player player, Quest quest, Date creationDate) {
        QueryRunner queryRunner = new QueryRunner(dataSource);
        try {
            queryRunner.insert("INSERT INTO PlayerDailyQuest (playerId, questId, creationDate) VALUES(?, ?, ?);",
                    new ScalarHandler<>(),
                    player.getId(),
                    quest.getId(),
                    creationDate);
        } catch (SQLException e) {
            throw new GatewayError("Failed to add daily quest to player.", e);
        }
    }

    @Override
    public void addCompletedHiddenQuestId(long playerId, long questId) {
        QueryRunner queryRunner = new QueryRunner(dataSource);
        try {
            queryRunner.insert("INSERT INTO PlayerHiddenQuest (playerId, questId) VALUES(?, ?);",
                    new ScalarHandler<>(),
                    playerId,
                    questId);
        } catch (SQLException e) {
            throw new GatewayError("Failed to add a completed hidden quest to player.", e);
        }
    }
}
