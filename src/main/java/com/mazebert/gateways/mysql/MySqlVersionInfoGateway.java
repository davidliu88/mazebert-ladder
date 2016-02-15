package com.mazebert.gateways.mysql;

import com.mazebert.entities.VersionInfo;
import com.mazebert.gateways.VersionInfoGateway;
import com.mazebert.gateways.error.GatewayError;
import org.apache.commons.dbutils.handlers.BeanHandler;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.sql.SQLException;

public class MySqlVersionInfoGateway extends MySqlGateway implements VersionInfoGateway {
    @Inject
    public MySqlVersionInfoGateway(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public VersionInfo findVersionInfo(String store) {
        try {
            return getQueryRunner().query("SELECT store, version, url, details FROM VersionInfo WHERE store=?;",
                    new BeanHandler<>(VersionInfo.class),
                    store);
        } catch (SQLException e) {
            throw new GatewayError("Failed to find version info for store.", e);
        }
    }
}
