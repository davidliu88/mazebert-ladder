package com.mazebert.gateways.mysql.connection;

import javax.sql.DataSource;

public interface DataSourceProvider {
    void prepare();
    DataSource get();
    void dispose();
}
