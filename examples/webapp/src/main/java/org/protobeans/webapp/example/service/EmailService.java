package org.protobeans.webapp.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    @Async
    public void sendMessage(String email, String text) {
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mailSender.createMimeMessage(), false, "UTF-8");
            
            helper.setFrom("support@example.com", "Protobeans Team");
            helper.setTo(email);
            helper.setSubject("Protobeans Support");
            helper.setText(text, true);
    
            mailSender.send(helper.getMimeMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
