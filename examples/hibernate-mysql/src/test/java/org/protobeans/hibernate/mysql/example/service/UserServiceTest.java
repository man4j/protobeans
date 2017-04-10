package org.protobeans.hibernate.mysql.example.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.protobeans.hibernate.mysql.example.Main;
import org.protobeans.hibernate.mysql.example.model.User;
import org.protobeans.testcontainers.mysql.annotation.EnableMySqlContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * add DOCKER_HOST,DOCKER_TLS_VERIFY,DOCKER_CERT_PATH in OS env vars
 * f.e. 
 * DOCKER_HOST=tcp://kaizen-retail.com:2376
 * DOCKER_CERT_PATH=C:\Users\home1\.docker
 * DOCKER_TLS_VERIFY=1
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes={Main.class})
@EnableMySqlContainer(user = "test", password = "test", exposeUrlAs = "url", exposeUserAs = "user", exposePasswordAs = "password")
public class UserServiceTest {
    @Autowired
    private UserService userService;
    
    @Test
    public void shouldWork() {
        Assert.assertEquals(0, userService.count());
        
        userService.insertUsers(10);
        
        Assert.assertEquals(10, userService.count());
    }
    
    @Test
    public void shouldUpdateDetached() {
        User u = new User("email", "pwd");
        
        userService.saveOrUpdate(u);
        
        u.setEmail("email1");
        
        userService.saveOrUpdate(u);
    }
    
}
