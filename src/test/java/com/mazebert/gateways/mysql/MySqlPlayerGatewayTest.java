package com.mazebert.gateways.mysql;

import com.mazebert.gateways.PlayerGatewayTest;
import org.junit.Before;

public class MySqlPlayerGatewayTest extends PlayerGatewayTest {
    @Before
    public void setUp() throws Exception {
        gateway = new MySqlPlayerGateway(TestDataSourceProvider.instance.getDataSource());
    }
}