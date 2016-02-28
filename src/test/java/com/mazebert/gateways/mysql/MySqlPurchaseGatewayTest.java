package com.mazebert.gateways.mysql;

import com.mazebert.gateways.PurchaseGatewayTest;
import com.mazebert.gateways.mysql.connection.TestDataSourceProvider;
import com.mazebert.gateways.mysql.connection.TestErrorDataSourceProvider;
import org.junit.Before;

public class MySqlPurchaseGatewayTest extends PurchaseGatewayTest {
    @Before
    public void setUp() throws Exception {
        gateway = new MySqlPurchaseGateway(TestDataSourceProvider.instance.get());
        errorGateway = new MySqlPurchaseGateway(TestErrorDataSourceProvider.instance.get());

        playerGateway = new MySqlPlayerGateway(TestDataSourceProvider.instance.get());
    }
}
