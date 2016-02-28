package com.mazebert.gateways.mysql.connection;

import com.google.inject.Provider;
import com.mazebert.plugins.system.EnvironmentPlugin;

import javax.inject.Inject;

public class CredentialsProvider implements Provider<Credentials> {
    private final EnvironmentPlugin environmentPlugin;

    @Inject
    public CredentialsProvider(EnvironmentPlugin environmentPlugin) {
        this.environmentPlugin = environmentPlugin;
    }

    @Override
    public Credentials get() {
        return new Credentials(
                resolveProperty("MAZEBERT_DB_USER"),
                resolveProperty("MAZEBERT_DB_PASSWORD"),
                resolveProperty("MAZEBERT_DB_URL")
        );
    }

    private String resolveProperty(String property) {
        return environmentPlugin.getEnvironmentVariable(property);
    }
}
