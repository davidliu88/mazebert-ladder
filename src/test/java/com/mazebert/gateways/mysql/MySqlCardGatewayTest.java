package com.mazebert.gateways.mysql;

import com.mazebert.gateways.CardGatewayTest;
import com.mazebert.gateways.mysql.connection.TestDataSourceProvider;
import com.mazebert.gateways.mysql.connection.TestErrorDataSourceProvider;
import org.junit.Before;

public class MySqlCardGatewayTest extends CardGatewayTest {
    @Before
    public void setUp() throws Exception {
        gateway = new MySqlCardGateway(TestDataSourceProvider.instance.get());
        errorGateway = new MySqlCardGateway(TestErrorDataSourceProvider.instance.get());
    }
}