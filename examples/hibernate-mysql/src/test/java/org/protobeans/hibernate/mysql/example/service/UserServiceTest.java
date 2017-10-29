package org.protobeans.hibernate.mysql.example.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.protobeans.hibernate.mysql.example.Main;
import org.protobeans.hibernate.mysql.example.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@ContextConfiguration(classes={Main.class})
public class UserServiceTest {
    @Autowired
    private UserService userService;
    
    @Test
    public void updateVsMerge() {
        Assert.assertEquals(0, userService.count());
        
        userService.insertUsers(10);
        
        userService.update(new User(0, "user" + 0 + "@mail.com", "pw" + 0, "1"));
        
        userService.checkAndUpdate(new User(0, "user" + 0 + "@mail.com", "pw" + 0, "1"));
        
        Assert.assertEquals(10, userService.count());
    }
}
