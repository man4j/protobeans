package org.protobeans.mail;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.protobeans.mail.annotation.EnableMail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DirtiesContext
@EnableMail(host = "${EMAIL_HOST}", user = "${EMAIL_USER}", password = "${EMAIL_PASSWORD}")
@ContextConfiguration(classes = SendTest.class)
public class SendTest {
    @Autowired JavaMailSender javaMailSender;
    
    @Value("${EMAIL_FROM}") String from;
    
    @Value("${EMAIL_TO}") String to;
    
    @Test
    public void shouldSend() {
        SimpleMailMessage simpleMessage = new SimpleMailMessage();
        
        simpleMessage.setFrom(from);
        simpleMessage.setSubject("Hello!");
        simpleMessage.setText("This is text!");
        simpleMessage.setTo(to);
        
        javaMailSender.send(simpleMessage);
    }
}
