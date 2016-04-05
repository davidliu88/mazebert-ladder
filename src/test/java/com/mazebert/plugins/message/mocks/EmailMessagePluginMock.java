package com.mazebert.plugins.message.mocks;

import com.mazebert.error.InternalServerError;
import com.mazebert.plugins.message.EmailMessage;
import com.mazebert.plugins.message.EmailMessagePlugin;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class EmailMessagePluginMock implements EmailMessagePlugin {
    private List<EmailMessage> sentMessages = new ArrayList<>();
    private List<EmailMessage> sentAsyncMessages = new ArrayList<>();
    private boolean deliveryWillFail;

    @Override
    public void sendEmail(EmailMessage emailMessage) {
        if (deliveryWillFail) {
            throw new InternalServerError("Failed to send email to " + emailMessage.getReceiver());
        }
        sentMessages.add(emailMessage);
    }

    @Override
    public void sendEmailAsync(EmailMessage emailMessage) {
        sentAsyncMessages.add(emailMessage);
    }

    public void givenDeliveryFails() {
        deliveryWillFail = true;
    }

    public EmailMessage getSentMessage(int index) {
        return sentMessages.get(index);
    }

    public EmailMessage getSentAsyncMessage(int index) {
        return sentMessages.get(index);
    }

    public void thenNoMessageIsSent() {
        assertEquals(0, sentMessages.size());
        assertEquals(0, sentAsyncMessages.size());
    }
}
