package com.mazebert.gateways.mysql.connection;

import javax.sql.DataSource;

public interface DataSourceProvider {
    DataSource getDataSource();
}
