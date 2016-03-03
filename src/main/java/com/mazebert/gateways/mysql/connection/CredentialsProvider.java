package com.mazebert.gateways.mysql.connection;

import com.google.inject.Provider;
import com.mazebert.plugins.system.SettingsPlugin;

import javax.inject.Inject;

public class CredentialsProvider implements Provider<Credentials> {
    private final SettingsPlugin settings;

    @Inject
    public CredentialsProvider(SettingsPlugin settings) {
        this.settings = settings;
    }

    @Override
    public Credentials get() {
        return new Credentials(
                resolveProperty(SettingsPlugin.DB_USER),
                resolveProperty(SettingsPlugin.DB_PASSWORD),
                resolveProperty(SettingsPlugin.DB_URL)
        );
    }

    private String resolveProperty(String property) {
        return settings.getRequiredProperty(property);
    }
}
