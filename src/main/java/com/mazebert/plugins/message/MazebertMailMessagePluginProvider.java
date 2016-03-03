package com.mazebert.plugins.message;

import com.mazebert.plugins.system.SettingsPlugin;

import javax.inject.Inject;

public class MazebertMailMessagePluginProvider extends JavaMailSmtpMessagePluginProvider {
    @Inject
    public MazebertMailMessagePluginProvider(SettingsPlugin settings) {
        super("mail.your-server.de", 587,
                "postmaster@mazebert.com", settings.getRequiredProperty(SettingsPlugin.EMAIL_PASSWORD),
                "postmaster@mazebert.com", "Mazebert TD");
    }
}
