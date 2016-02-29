package com.mazebert.gateways.mysql;

import com.mazebert.gateways.SupporterGatewayTest;
import com.mazebert.gateways.mysql.connection.TestDataSourceProvider;
import com.mazebert.gateways.mysql.connection.TestErrorDataSourceProvider;
import org.junit.Before;

public class MySqlSupporterGatewayTest extends SupporterGatewayTest {
    @Before
    public void setUp() throws Exception {
        gateway = new MySqlSupporterGateway(TestDataSourceProvider.instance.get());
        errorGateway = new MySqlSupporterGateway(TestErrorDataSourceProvider.instance.get());

        playerGateway = new MySqlPlayerGateway(TestDataSourceProvider.instance.get());
    }
}