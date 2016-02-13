package com.mazebert.gateways.mysql;

import com.mazebert.gateways.FoilCardGatewayTest;
import org.junit.Before;

public class MySqlFoilCardGatewayTest extends FoilCardGatewayTest {
    @Before
    public void setUp() throws Exception {
        gateway = new MySqlFoilCardGateway(TestDataSourceProvider.instance.getDataSource());
        errorGateway = new MySqlFoilCardGateway(TestErrorDataSourceProvider.instance.getDataSource());
    }
}
