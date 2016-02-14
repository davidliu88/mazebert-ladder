package com.mazebert.gateways.mysql;

import com.mazebert.entities.BlackMarketSettings;
import com.mazebert.gateways.BlackMarketSettingsGateway;
import com.mazebert.gateways.error.GatewayError;
import org.apache.commons.dbutils.handlers.BeanHandler;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.sql.SQLException;

public class MySqlBlackMarketSettingsGateway extends MySqlGateway implements BlackMarketSettingsGateway {
    @Inject
    public MySqlBlackMarketSettingsGateway(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public BlackMarketSettings getSettings() {
        try {
            return getQueryRunner().query("SELECT price FROM BlackMarket WHERE id=1;",
                    new BeanHandler<>(BlackMarketSettings.class));
        } catch (SQLException e) {
            throw new GatewayError("Failed to read black market settings.", e);
        }
    }
}
