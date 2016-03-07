package com.mazebert.plugins.message;

import com.google.inject.Provider;
import com.mazebert.categories.IntegrationTest;
import com.mazebert.plugins.system.SettingsPlugin;
import com.mazebert.plugins.system.mocks.SettingsPluginMock;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.io.IOException;
import java.util.Properties;

import static org.jusecase.Builders.a;
import static org.jusecase.Builders.inputStream;

@Category(IntegrationTest.class)
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
        SettingsPluginMock settings = new SettingsPluginMock();
        settings.givenPropertyExists(SettingsPlugin.EMAIL_PASSWORD, getTestPassword());
        Provider<EmailMessagePlugin> provider = new MazebertMailMessagePluginProvider(settings);
        return provider.get();
    }

    private String getTestPassword() throws IOException {
        Properties properties = new Properties();
        properties.load(a(inputStream().withResource("integrationtest-mail.properties")));
        return properties.getProperty("password");
    }
}
