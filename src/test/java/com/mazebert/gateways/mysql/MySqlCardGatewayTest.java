package com.mazebert.gateways.mysql;

import com.mazebert.gateways.CardGatewayTest;
import org.junit.Before;

public class MySqlCardGatewayTest extends CardGatewayTest {
    @Before
    public void setUp() throws Exception {
        gateway = new MySqlCardGateway(TestDataSourceProvider.instance.getDataSource());
        errorGateway = new MySqlCardGateway(TestErrorDataSourceProvider.instance.getDataSource());
    }
}