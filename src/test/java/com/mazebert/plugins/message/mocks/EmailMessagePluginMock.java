package com.mazebert.plugins.message.mocks;

import com.mazebert.plugins.message.EmailMessage;
import com.mazebert.plugins.message.EmailMessagePlugin;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class EmailMessagePluginMock implements EmailMessagePlugin {
    private List<EmailMessage> sentMessages = new ArrayList<>();

    @Override
    public void sendEmail(EmailMessage emailMessage) {
        sentMessages.add(emailMessage);
    }

    public EmailMessage getSentMessage(int index) {
        return sentMessages.get(index);
    }

    public void thenNoMessageIsSent() {
        assertEquals(0, sentMessages.size());
    }
}
