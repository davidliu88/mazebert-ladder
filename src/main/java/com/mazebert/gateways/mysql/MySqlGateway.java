package com.mazebert.gateways.mysql;

import org.apache.commons.dbutils.QueryRunner;

import javax.sql.DataSource;

public abstract class MySqlGateway {
    protected final DataSource dataSource;

    public MySqlGateway(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    protected QueryRunner getQueryRunner() {
        return new QueryRunner(dataSource);
    }
}
