package com.mazebert.plugins.message;

import com.google.inject.Provider;
import com.mazebert.plugins.system.mocks.EnvironmentPluginMock;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.util.Properties;

import static org.jusecase.Builders.a;
import static org.jusecase.Builders.inputStream;

@Ignore
public class EmailMessagePluginTest {
    private EmailMessagePlugin emailMessagePlugin;

    @Before
    public void setUp() throws Exception {
        emailMessagePlugin = createPlugin();
    }

    @Test
    public void sendEmail() {
        EmailMessage message = new EmailMessage();
        message.setReceiver("andy@mazebert.com");
        message.setSubject("EmailMessagePlugin integration test");
        message.setMessage("Hi!\n\nIf you can read this message, I've been successful.\n\nBye!");
        emailMessagePlugin.sendEmail(message);
    }

    private EmailMessagePlugin createPlugin() throws IOException {
        EnvironmentPluginMock environmentPlugin = new EnvironmentPluginMock();
        environmentPlugin.givenEnvironmentVariableExists("MAZEBERT_EMAIL_PASSWORD", getTestPassword());
        Provider<EmailMessagePlugin> provider = new MazebertMailMessagePluginProvider(environmentPlugin);
        return provider.get();
    }

    private String getTestPassword() throws IOException {
        Properties properties = new Properties();
        properties.load(a(inputStream().withResource("integrationtest-mail.properties")));
        return properties.getProperty("password");
    }
}
