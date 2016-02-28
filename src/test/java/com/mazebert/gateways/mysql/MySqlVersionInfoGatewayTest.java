package com.mazebert.gateways.mysql;

import com.mazebert.gateways.VersionInfoGatewayTest;
import com.mazebert.gateways.mysql.connection.TestDataSourceProvider;
import com.mazebert.gateways.mysql.connection.TestErrorDataSourceProvider;
import org.junit.Before;

public class MySqlVersionInfoGatewayTest extends VersionInfoGatewayTest {
    @Before
    public void setUp() throws Exception {
        gateway = new MySqlVersionInfoGateway(TestDataSourceProvider.instance.get());
        errorGateway = new MySqlVersionInfoGateway(TestErrorDataSourceProvider.instance.get());
    }
}