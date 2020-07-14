package org.protobeans.mail;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.protobeans.mail.annotation.EnableMail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DirtiesContext
@EnableMail(host = "", user = "", password = "")
@ContextConfiguration(classes = SendTest.class)
public class SendTest {
    @Autowired
    private JavaMailSender javaMailSender;
    
    //@Test
    public void shouldSend() {
        SimpleMailMessage simpleMessage = new SimpleMailMessage();
        
        simpleMessage.setFrom("support@i9m.app");
        simpleMessage.setSubject("Hello!");
        simpleMessage.setText("This is text!");
        simpleMessage.setTo("man4j@ya.ru");
        
        javaMailSender.send(simpleMessage);
    }
}
