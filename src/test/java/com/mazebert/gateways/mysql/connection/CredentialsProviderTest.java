package com.mazebert.gateways.mysql.connection;

import com.mazebert.plugins.system.mocks.EnvironmentPluginMock;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CredentialsProviderTest {
    private CredentialsProvider provider;
    private EnvironmentPluginMock environmentPlugin = new EnvironmentPluginMock();

    private Credentials credentials;

    @Before
    public void setUp() throws Exception {
        provider = new CredentialsProvider(environmentPlugin);
    }

    @Test
    public void credentialsAreFetchedCorrectly() {
        environmentPlugin.givenEnvironmentVariableExists("MAZEBERT_DB_USER", "user");
        environmentPlugin.givenEnvironmentVariableExists("MAZEBERT_DB_PASSWORD", "password");
        environmentPlugin.givenEnvironmentVariableExists("MAZEBERT_DB_URL", "localhost/my_database");

        whenGetCredentials();

        assertEquals("user", credentials.getUser());
        assertEquals("password", credentials.getPassword());
        assertEquals("localhost/my_database", credentials.getUrl());
    }

    private void whenGetCredentials() {
        credentials = provider.get();
    }
}