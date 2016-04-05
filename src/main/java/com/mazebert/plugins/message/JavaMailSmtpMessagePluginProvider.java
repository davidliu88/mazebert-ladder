package com.mazebert.plugins.message;

import com.google.inject.Provider;
import com.mazebert.error.InternalServerError;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.internet.InternetAddress;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.logging.Logger;

public class JavaMailSmtpMessagePluginProvider implements Provider<EmailMessagePlugin> {
    private final Properties properties;
    private final Authenticator authenticator;
    private final InternetAddress from;
    private final ExecutorService executorService;
    private final Logger logger;

    public JavaMailSmtpMessagePluginProvider(String host, int port, String userName, String password, String fromAddress, String fromName,
                                             ExecutorService executorService, Logger logger) {
        properties = createProperties(host, port);
        authenticator = createAuthenticator(userName, password);
        from = createFromAddress(fromAddress, fromName);
        this.executorService = executorService;
        this.logger = logger;
    }

    private InternetAddress createFromAddress(String fromAddress, String fromName) {
        try {
            return new InternetAddress(fromAddress, fromName);
        } catch (UnsupportedEncodingException e) {
            throw new InternalServerError("Invalid email address format.", e);
        }
    }

    protected Properties createProperties(String host, int port) {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "" + port);
        properties.put("mail.transport.protocol","smtp");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.tls", "true");
        properties.put("mail.smtp.ssl.checkserveridentity", "true");
        return properties;
    }

    protected Authenticator createAuthenticator(final String userName, final String password) {
        return new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userName, password);
            }
        };
    }

    @Override
    public EmailMessagePlugin get() {
        return new JavaMailMessagePlugin(properties, authenticator, from, executorService, logger);
    }
}
