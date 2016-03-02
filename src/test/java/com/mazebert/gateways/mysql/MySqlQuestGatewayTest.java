package com.mazebert.gateways.mysql;

import com.mazebert.gateways.QuestGatewayTest;
import com.mazebert.gateways.mysql.connection.TestDataSourceProvider;
import com.mazebert.gateways.mysql.connection.TestErrorDataSourceProvider;
import org.junit.Before;

public class MySqlQuestGatewayTest extends QuestGatewayTest {
    @Before
    public void setUp() throws Exception {
        gateway = new MySqlQuestGateway(TestDataSourceProvider.instance.get());
        errorGateway = new MySqlQuestGateway(TestErrorDataSourceProvider.instance.get());

        playerGateway = new MySqlPlayerGateway(TestDataSourceProvider.instance.get());
    }
}