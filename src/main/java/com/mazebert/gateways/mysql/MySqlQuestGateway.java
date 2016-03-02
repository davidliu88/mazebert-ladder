package com.mazebert.gateways.mysql;

import com.google.common.collect.Lists;
import com.google.common.primitives.Longs;
import com.mazebert.entities.Player;
import com.mazebert.entities.Quest;
import com.mazebert.gateways.QuestGateway;
import com.mazebert.gateways.error.GatewayError;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class MySqlQuestGateway extends MySqlGateway implements QuestGateway {
    @Inject
    public MySqlQuestGateway(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public List<Long> findCompletedHiddenQuestIds(long playerId) {
        try {
            List<Integer> ids = getQueryRunner().query("SELECT questId FROM PlayerHiddenQuest WHERE playerId=?;",
                    new ColumnListHandler<>(1),
                    playerId);
            return Lists.transform(ids, Integer::longValue);
        } catch (SQLException e) {
            throw new GatewayError("Failed to find completed hidden quest ids for player.", e);
        }
    }

    @Override
    public List<Quest> findDailyQuests(long playerId) {
        try {
            return getQueryRunner().query("SELECT questId AS id, reward, isHidden, sinceVersion, requiredAmount FROM PlayerDailyQuest, Quest WHERE playerId=? AND Quest.id=PlayerDailyQuest.questId ORDER BY creationDate ASC;",
                    new BeanListHandler<>(Quest.class),
                    playerId);
        } catch (SQLException e) {
            throw new GatewayError("Failed to find daily quests for player.", e);
        }
    }

    @Override
    public List<Long> findDailyQuestIds(long playerId) {
        try {
            List<Integer> ids = getQueryRunner().query("SELECT questId FROM PlayerDailyQuest WHERE playerId=? ORDER BY creationDate ASC;",
                    new ColumnListHandler<>(1),
                    playerId);
            return Lists.transform(ids, Integer::longValue);
        } catch (SQLException e) {
            throw new GatewayError("Failed to determine daily quest ids for player.", e);
        }
    }

    @Override
    public List<Quest> findAllQuests() {
        try {
            return getQueryRunner().query("SELECT id, reward, isHidden, sinceVersion, requiredAmount FROM Quest;",
                    new BeanListHandler<>(Quest.class));
        } catch (SQLException e) {
            throw new GatewayError("Failed to determine all available quests.", e);
        }
    }

    @Override
    public List<Quest> findQuestsByIds(List<Long> questIds) {
        try {
            String sequence = Longs.join(",", Longs.toArray(questIds));
            return getQueryRunner().query("SELECT id, reward, isHidden, sinceVersion, requiredAmount FROM Quest WHERE id IN(" + sequence + ");",
                    new BeanListHandler<>(Quest.class));
        } catch (SQLException e) {
            throw new GatewayError("Failed to find quests by ids in database.", e);
        }
    }

    @Override
    public void addDailyQuest(Player player, Quest quest, Date creationDate) {
        try {
            getQueryRunner().insert("INSERT INTO PlayerDailyQuest (playerId, questId, creationDate) VALUES(?, ?, ?);",
                    new ScalarHandler<>(),
                    player.getId(),
                    quest.getId(),
                    creationDate);
        } catch (SQLException e) {
            throw new GatewayError("Failed to add daily quest to player.", e);
        }
    }

    @Override
    public void removeDailyQuest(Player player, long questId) {
        try {
            getQueryRunner().update("DELETE FROM PlayerDailyQuest WHERE playerId=? AND questId=?;",
                    player.getId(),
                    questId);
        } catch (SQLException e) {
            throw new GatewayError("Failed to remove daily quest in database.", e);
        }
    }

    @Override
    public void replaceDailyQuest(long playerId, long oldQuestId, long newQuestId, Date creationDate) {
        // TODO implement me!
    }

    @Override
    public void addCompletedHiddenQuestId(long playerId, long questId) {
        try {
            getQueryRunner().insert("INSERT INTO PlayerHiddenQuest (playerId, questId) VALUES(?, ?);",
                    new ScalarHandler<>(),
                    playerId,
                    questId);
        } catch (SQLException e) {
            throw new GatewayError("Failed to add a completed hidden quest to player.", e);
        }
    }
}
