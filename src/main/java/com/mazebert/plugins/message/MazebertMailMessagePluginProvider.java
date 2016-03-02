package com.mazebert.plugins.message;

import com.mazebert.plugins.system.EnvironmentPlugin;

import javax.inject.Inject;

public class MazebertMailMessagePluginProvider extends JavaMailSmtpMessagePluginProvider {
    @Inject
    public MazebertMailMessagePluginProvider(EnvironmentPlugin environmentPlugin) {
        super("mail.your-server.de", 587,
                "postmaster@mazebert.com", environmentPlugin.getEnvironmentVariable("MAZEBERT_EMAIL_PASSWORD"),
                "postmaster@mazebert.com", "Mazebert TD");
    }
}
