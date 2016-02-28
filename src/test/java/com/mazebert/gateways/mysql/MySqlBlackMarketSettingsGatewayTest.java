package com.mazebert.gateways.mysql;

import com.mazebert.gateways.BlackMarketSettingsGatewayTest;
import com.mazebert.gateways.mysql.connection.TestDataSourceProvider;
import com.mazebert.gateways.mysql.connection.TestErrorDataSourceProvider;
import org.junit.Before;

public class MySqlBlackMarketSettingsGatewayTest extends BlackMarketSettingsGatewayTest {
    @Before
    public void setUp() throws Exception {
        gateway = new MySqlBlackMarketSettingsGateway(TestDataSourceProvider.instance.get());
        errorGateway = new MySqlBlackMarketSettingsGateway(TestErrorDataSourceProvider.instance.get());
    }
}