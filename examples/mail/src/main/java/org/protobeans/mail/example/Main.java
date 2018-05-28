package org.protobeans.mail.example;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;

import org.protobeans.core.EntryPoint;
import org.protobeans.mail.annotation.EnableMail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

@EnableMail(user = "s:gmailUser", password = "s:gmailPassword")
public class Main {
    @Autowired
    JavaMailSender mailSender;
    
    @EventListener(ContextRefreshedEvent.class)
    void sendEmail() throws MessagingException, UnsupportedEncodingException {
        MimeMessageHelper helper = new MimeMessageHelper(mailSender.createMimeMessage(), false, "UTF-8");
        
        helper.setFrom("support@example.com", "Protobeans Team");
        helper.setTo("man4j@ya.ru");
        helper.setSubject("Protobeans Support");
        helper.setText("Hello, <b>userName</b>!", true);

        mailSender.send(helper.getMimeMessage());
    }
    
    public static void main(String[] args) {
        EntryPoint.run(Main.class);
    }
}
