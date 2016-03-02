package com.mazebert.plugins.message;

import com.mazebert.error.InternalServerError;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

import static org.jusecase.Builders.an;
import static org.jusecase.Builders.array;

public class JavaMailMessagePlugin implements EmailMessagePlugin {
    private final Session session;
    private final InternetAddress[] from;

    public JavaMailMessagePlugin(Properties properties, Authenticator authenticator, InternetAddress from) {
        session = Session.getDefaultInstance(properties, authenticator);
        this.from = an(array(from));
    }

    @Override
    public void sendEmail(EmailMessage emailMessage) {
        try {
            Message message = new MimeMessage(session);
            message.addFrom(from);
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(emailMessage.getReceiver()));
            message.setSubject(emailMessage.getSubject());
            message.setText(emailMessage.getMessage());
            message.saveChanges();

            Transport.send(message);
        } catch (Throwable e) {
            throw new InternalServerError("Failed to send email to " + emailMessage.getReceiver(), e);
        }
    }
}
