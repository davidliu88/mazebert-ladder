package com.mazebert.plugins.security;

import com.mazebert.plugins.system.SettingsPlugin;

import javax.inject.Inject;

public class ServerContentSigner extends ContentSigner {
    @Inject
    public ServerContentSigner(SettingsPlugin settings) {
        super(
                settings.getRequiredProperty(SettingsPlugin.PRIVATE_KEY)
        );
    }
}
