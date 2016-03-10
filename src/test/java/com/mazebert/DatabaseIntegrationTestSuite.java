package com.mazebert;

import com.mazebert.gateways.mysql.connection.TestDataSourceProvider;
import org.junit.AfterClass;
import org.junit.BeforeClass;

public class DatabaseIntegrationTestSuite {
    @BeforeClass
    public static void setUp() throws Exception {
        TestDataSourceProvider.instance.prepare();
    }

    @AfterClass
    public static void tearDown() throws Exception {
        TestDataSourceProvider.instance.dispose();
    }
}
