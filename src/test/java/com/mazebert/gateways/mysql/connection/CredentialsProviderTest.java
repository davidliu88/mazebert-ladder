package com.mazebert.gateways.mysql.connection;

import com.mazebert.plugins.system.SettingsPlugin;
import com.mazebert.plugins.system.mocks.EnvironmentPluginMock;
import com.mazebert.plugins.system.mocks.SettingsPluginMock;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CredentialsProviderTest {
    private CredentialsProvider provider;
    private SettingsPluginMock settings = new SettingsPluginMock();

    private Credentials credentials;

    @Before
    public void setUp() throws Exception {
        provider = new CredentialsProvider(settings);
    }

    @Test
    public void credentialsAreFetchedCorrectly() {
        settings.givenPropertyExists(SettingsPlugin.DB_USER, "user");
        settings.givenPropertyExists(SettingsPlugin.DB_PASSWORD, "password");
        settings.givenPropertyExists(SettingsPlugin.DB_URL, "localhost/my_database");

        whenGetCredentials();

        assertEquals("user", credentials.getUser());
        assertEquals("password", credentials.getPassword());
        assertEquals("localhost/my_database", credentials.getUrl());
    }

    private void whenGetCredentials() {
        credentials = provider.get();
    }
}