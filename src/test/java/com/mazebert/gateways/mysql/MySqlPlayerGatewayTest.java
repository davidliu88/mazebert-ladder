package com.mazebert.gateways.mysql;

import com.mazebert.gateways.PlayerGatewayTest;
import com.mazebert.gateways.mysql.connection.TestDataSourceProvider;
import com.mazebert.gateways.mysql.connection.TestErrorDataSourceProvider;
import org.junit.Before;

public class MySqlPlayerGatewayTest extends PlayerGatewayTest {
    @Before
    public void setUp() throws Exception {
        gateway = new MySqlPlayerGateway(TestDataSourceProvider.instance.get());
        errorGateway = new MySqlPlayerGateway(TestErrorDataSourceProvider.instance.get());
    }
}