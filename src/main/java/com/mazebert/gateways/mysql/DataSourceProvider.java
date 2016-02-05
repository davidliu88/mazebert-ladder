package com.mazebert.gateways.mysql;

import javax.sql.DataSource;

public interface DataSourceProvider {
    DataSource getDataSource();
}
