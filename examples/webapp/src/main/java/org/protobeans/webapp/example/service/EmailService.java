package org.protobeans.webapp.example.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailService extends org.protobeans.mail.service.EmailService {
    @Value("${EMAIL_SEND_FROM:support@i9m.app}")
    private String emailSendFrom;
    
    @Value("${EMAIL_SEND_FROM_TITLE:Support team}")
    private String emailSendFromTitle;

    public void sendMessage(String email, String subject, String text) {
        super.sendMessage(email, subject, text, emailSendFrom, emailSendFromTitle);
    }
}
