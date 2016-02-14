package com.mazebert.gateways.mysql;

import com.mazebert.entities.BlackMarketSettings;
import com.mazebert.gateways.BlackMarketSettingsGateway;
import com.mazebert.gateways.error.GatewayError;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.sql.SQLException;

public class MySqlBlackMarketSettingsGateway implements BlackMarketSettingsGateway {
    private final DataSource dataSource;

    @Inject
    public MySqlBlackMarketSettingsGateway(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public BlackMarketSettings getSettings() {
        QueryRunner queryRunner = new QueryRunner(dataSource);
        try {
            return queryRunner.query("SELECT price FROM BlackMarket WHERE id=1;", new BeanHandler<>(BlackMarketSettings.class));
        } catch (SQLException e) {
            throw new GatewayError("Failed to read black market settings.", e);
        }
    }
}
