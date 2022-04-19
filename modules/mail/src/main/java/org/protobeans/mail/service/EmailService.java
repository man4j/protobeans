package org.protobeans.mail.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

public class EmailService {
    @Autowired
    private JavaMailSender mailSender;
    
    public void sendMessage(String email, String subject, String text, String emailSendFrom, String emailSendFromTitle) {
        new Thread() {
            @Override
            public void run() {
                try {
                    MimeMessageHelper helper = new MimeMessageHelper(mailSender.createMimeMessage(), false, "UTF-8");
        
                    helper.setFrom(emailSendFrom, emailSendFromTitle);
                    helper.setTo(email);
                    helper.setSubject(subject);
                    helper.setText(text, true);
        
                    mailSender.send(helper.getMimeMessage());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }.start();
    }
}
