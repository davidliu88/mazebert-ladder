package com.mazebert.gateways.mysql;

import com.mazebert.gateways.BlackMarketOfferGatewayTest;
import com.mazebert.gateways.mysql.connection.TestDataSourceProvider;
import com.mazebert.gateways.mysql.connection.TestErrorDataSourceProvider;
import org.junit.Before;

public class MySqlBlackMarketOfferGatewayTest extends BlackMarketOfferGatewayTest {
    @Before
    public void setUp() throws Exception {
        gateway = new MySqlBlackMarketOfferGateway(TestDataSourceProvider.instance.getDataSource());
        errorGateway = new MySqlBlackMarketOfferGateway(TestErrorDataSourceProvider.instance.getDataSource());
    }
}