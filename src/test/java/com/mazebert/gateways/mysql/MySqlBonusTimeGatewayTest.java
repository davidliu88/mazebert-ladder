package com.mazebert.gateways.mysql;

import com.mazebert.gateways.BonusTimeGatewayTest;
import com.mazebert.gateways.mysql.connection.TestDataSourceProvider;
import com.mazebert.gateways.mysql.connection.TestErrorDataSourceProvider;
import org.junit.Before;

public class MySqlBonusTimeGatewayTest extends BonusTimeGatewayTest {
    @Before
    public void setUp() throws Exception {
        gateway = new MySqlBonusTimeGateway(TestDataSourceProvider.instance.getDataSource());
        errorGateway = new MySqlBonusTimeGateway(TestErrorDataSourceProvider.instance.getDataSource());

        playerGateway = new MySqlPlayerGateway(TestDataSourceProvider.instance.getDataSource());
    }
}