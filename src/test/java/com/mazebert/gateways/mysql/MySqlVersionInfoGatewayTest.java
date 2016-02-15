package com.mazebert.gateways.mysql;

import com.mazebert.gateways.VersionInfoGatewayTest;
import org.junit.Before;

public class MySqlVersionInfoGatewayTest extends VersionInfoGatewayTest {
    @Before
    public void setUp() throws Exception {
        gateway = new MySqlVersionInfoGateway(TestDataSourceProvider.instance.getDataSource());
        errorGateway = new MySqlVersionInfoGateway(TestErrorDataSourceProvider.instance.getDataSource());
    }
}