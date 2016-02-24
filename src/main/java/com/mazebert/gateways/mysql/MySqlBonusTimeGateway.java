package com.mazebert.gateways.mysql;

import com.mazebert.entities.PlayerBonusTime;
import com.mazebert.entities.Version;
import com.mazebert.gateways.BonusTimeGateway;
import com.mazebert.gateways.error.GatewayError;
import com.mazebert.usecases.bonustime.GetBonusTimes;
import com.mazebert.usecases.bonustime.UpdateBonusTime;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MySqlBonusTimeGateway extends MySqlGateway implements BonusTimeGateway {

    private static final int WAVE_TYPE_AMOUNT = 4;
    private static final int DIFFICULTY_AMOUNT = 3;

    @Inject
    public MySqlBonusTimeGateway(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public List<PlayerBonusTime> findBonusTimes(GetBonusTimes.Request request) {
        try {
            String tableName = getTableName(request);
            String bonusTimeStatement = getBonusTimeStatement(request);
            String sql = "SELECT Player.name AS name, Player.id, " + bonusTimeStatement + " AS bonusTime FROM " + tableName + " LEFT JOIN Player ON Player.id=" + tableName + ".playerId WHERE mapId=? AND Player.isCheater=0 AND " + bonusTimeStatement + ">0 ORDER BY bonusTime DESC, playerId ASC LIMIT ?, ?;";
            List<PlayerBonusTime> result = getQueryRunner().query(sql,
                    new BeanListHandler<>(PlayerBonusTime.class),
                    request.mapId,
                    request.start,
                    request.limit);
            addRankToResult(result, request.start);
            return result;
        } catch (SQLException e) {
            throw new GatewayError("Could not find bonus times in database.", e);
        }
    }

    private String getTableName(GetBonusTimes.Request request) {
        if ("*".equals(request.appVersion)) {
            return "BonusTime";
        }

        Version version = new Version(request.appVersion);
        return "BonusTime_" + version.getMajor() + "_" + version.getMinor() + "_" + version.getBugfix();
    }

    private String getBonusTimeStatement(GetBonusTimes.Request request) {
        if (isAsterisk(request.difficultyType) && isAsterisk(request.waveAmountType)) {
            return "bonusTimeMax";
        }
        else if (isAsterisk(request.waveAmountType)) {
            return createMaxWaveAmountStatement(request.difficultyType);
        }
        else if (isAsterisk(request.difficultyType)) {
            return createMaxDifficultyStatement(request.waveAmountType);
        }

        return "bonusTime" + request.difficultyType + request.waveAmountType;
    }

    private String createMaxWaveAmountStatement(String difficultyType) {
        List<String> rows = new ArrayList<>();
        for (int waveAmountType = 0; waveAmountType < WAVE_TYPE_AMOUNT; ++waveAmountType) {
            rows.add("bonusTime" + difficultyType + waveAmountType);
        }
        return createMaxStatement(rows);
    }

    private String createMaxDifficultyStatement(String waveAmountType) {
        List<String> rows = new ArrayList<>();
        for (int difficultyType = 0; difficultyType < DIFFICULTY_AMOUNT; ++difficultyType) {
            rows.add("bonusTime" + difficultyType + waveAmountType);
        }
        return createMaxStatement(rows);
    }

    private String createMaxStatement(List<String> rows) {
        return "GREATEST(" + String.join(",", rows) + ")";
    }

    private boolean isAsterisk(String parameter) {
        return "*".equals(parameter);
    }

    private void addRankToResult(List<PlayerBonusTime> result, int offset) {
        int rank = offset;
        for (PlayerBonusTime time : result) {
            time.setRank(++rank);
        }
    }

    @Override
    public void updateBonusTime(UpdateBonusTime.Request request) {
        try(Connection connection = dataSource.getConnection()) {
            CurrentBonusTime currentBonusTime = findCurrentBonusTime(connection, request);
            if (currentBonusTime != null) {
                updateBonusTime(connection, currentBonusTime, request);
            } else {
                insertBonusTime(connection, request);
            }
        } catch (SQLException e) {
            throw new GatewayError("Could not update bonus time in database.", e);
        }
    }

    private CurrentBonusTime findCurrentBonusTime(Connection connection, UpdateBonusTime.Request request) throws SQLException {
        return getQueryRunner().query(connection,
                "SELECT id, bonusTimeMax, " + getBonusTimeColumnName(request) + " AS bonusTime FROM BonusTime WHERE playerId=? AND mapId=?;",
                new BeanHandler<>(CurrentBonusTime.class),
                request.playerId,
                request.mapId);
    }

    private void insertBonusTime(Connection connection, UpdateBonusTime.Request request) throws SQLException {
        getQueryRunner().insert(connection, "INSERT INTO BonusTime (playerId, mapId, bonusTimeMax, " + getBonusTimeColumnName(request) + ") VALUES(?, ?, ?, ?);",
                new ScalarHandler<>(),
                request.playerId,
                request.mapId,
                request.secondsSurvived,
                request.secondsSurvived);
    }

    private void updateBonusTime(Connection connection, CurrentBonusTime currentBonusTime, UpdateBonusTime.Request request) throws SQLException {
        getQueryRunner().update(connection, "UPDATE BonusTime SET bonusTimeMax=?, " + getBonusTimeColumnName(request) + "=? WHERE id=?;",
                Math.max(currentBonusTime.getBonusTimeMax(), request.secondsSurvived),
                Math.max(currentBonusTime.getBonusTime(), request.secondsSurvived),
                currentBonusTime.getId());
    }

    private String getBonusTimeColumnName(UpdateBonusTime.Request request) {
        return "bonusTime" + request.difficultyType + request.waveAmountType;
    }

    public static class CurrentBonusTime {
        private long id;
        private int bonusTimeMax;
        private int bonusTime;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public int getBonusTimeMax() {
            return bonusTimeMax;
        }

        public void setBonusTimeMax(int bonusTimeMax) {
            this.bonusTimeMax = bonusTimeMax;
        }

        public int getBonusTime() {
            return bonusTime;
        }

        public void setBonusTime(int bonusTime) {
            this.bonusTime = bonusTime;
        }
    }
}
