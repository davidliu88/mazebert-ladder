package com.mazebert.plugins.message;

public interface EmailMessagePlugin {
    void sendEmail(EmailMessage emailMessage);
    void sendEmailAsync(EmailMessage emailMessage);
}
