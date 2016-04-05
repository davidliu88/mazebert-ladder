package com.mazebert.plugins.message;

import com.mazebert.plugins.system.SettingsPlugin;

import javax.inject.Inject;
import java.util.concurrent.ExecutorService;
import java.util.logging.Logger;

public class MazebertMailMessagePluginProvider extends JavaMailSmtpMessagePluginProvider {
    @Inject
    public MazebertMailMessagePluginProvider(SettingsPlugin settings, ExecutorService executorService, Logger logger) {
        super("mail.your-server.de", 587,
                "postmaster@mazebert.com", settings.getRequiredProperty(SettingsPlugin.EMAIL_PASSWORD),
                "postmaster@mazebert.com", "Mazebert TD",
                executorService, logger);
    }
}
