package org.protobeans.webapp.example.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmailService extends org.protobeans.mail.service.EmailService {
    @Value("${EMAIL_SEND_FROM:support@i9m.app}")
    private String emailSendFrom;
    
    @Value("${EMAIL_SEND_FROM_TITLE:Support team}")
    private String emailSendFromTitle;

    public void sendMessage(String email, String subject, String text) {
        log.info("Email: " + email + ", subject: " + subject + ", text: " + text);
        // super.sendMessage(email, subject, text, emailSendFrom, emailSendFromTitle);
    }
}
