package com.mazebert.gateways.mysql;

import com.mazebert.entities.Supporter;
import com.mazebert.gateways.SupporterGateway;
import com.mazebert.gateways.error.GatewayError;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

public class MySqlSupporterGateway extends MySqlGateway implements SupporterGateway {

    @Inject
    public MySqlSupporterGateway(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public List<Supporter> findAllSupporters() {
        try {
            return getQueryRunner().query("SELECT id, name, supporterLevel FROM Player WHERE supporterLevel>0 ORDER BY supporterLevel DESC, LOWER(name) ASC;",
                    new BeanListHandler<>(Supporter.class));
        } catch (SQLException e) {
            throw new GatewayError("Failed to find all supporters.", e);
        }
    }
}
