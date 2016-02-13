package com.mazebert.gateways.mysql;

import com.mazebert.gateways.QuestGatewayTest;
import org.junit.Before;

public class MySqlQuestGatewayTest extends QuestGatewayTest {
    @Before
    public void setUp() throws Exception {
        gateway = new MySqlQuestGateway(TestDataSourceProvider.instance.getDataSource());
        errorGateway = new MySqlQuestGateway(TestErrorDataSourceProvider.instance.getDataSource());
    }
}