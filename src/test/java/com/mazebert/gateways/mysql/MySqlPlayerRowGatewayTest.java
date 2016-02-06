package com.mazebert.gateways.mysql;

import com.mazebert.gateways.PlayerRowGatewayTest;
import org.junit.Before;

public class MySqlPlayerRowGatewayTest extends PlayerRowGatewayTest {
    @Before
    public void setUp() throws Exception {
        gateway = new MySqlPlayerRowGateway(TestDataSourceProvider.instance.getDataSource());
        playerGateway = new MySqlPlayerGateway(TestDataSourceProvider.instance.getDataSource());
    }
}
