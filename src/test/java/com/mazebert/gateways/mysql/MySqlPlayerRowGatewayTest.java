package com.mazebert.gateways.mysql;

import com.mazebert.gateways.PlayerRowGatewayTest;
import com.mazebert.gateways.mysql.connection.TestDataSourceProvider;
import com.mazebert.gateways.mysql.connection.TestErrorDataSourceProvider;
import org.junit.Before;

public class MySqlPlayerRowGatewayTest extends PlayerRowGatewayTest {
    @Before
    public void setUp() throws Exception {
        gateway = new MySqlPlayerRowGateway(TestDataSourceProvider.instance.getDataSource());
        errorGateway = new MySqlPlayerRowGateway(TestErrorDataSourceProvider.instance.getDataSource());

        playerGateway = new MySqlPlayerGateway(TestDataSourceProvider.instance.getDataSource());
    }
}
