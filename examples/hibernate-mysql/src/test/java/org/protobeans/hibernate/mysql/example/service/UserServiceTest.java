package org.protobeans.hibernate.mysql.example.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.protobeans.hibernate.mysql.example.Main;
import org.protobeans.hibernate.mysql.example.model.User;
import org.protobeans.testcontainers.mysql.annotation.EnablePerconaContainer;
import org.protobeans.testcontainers.mysql.listener.PerconaContainerListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestExecutionListeners.MergeMode;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * add DOCKER_HOST,DOCKER_TLS_VERIFY,DOCKER_CERT_PATH in OS env vars
 * f.e. 
 * DOCKER_HOST=tcp://example.com:2376
 * DOCKER_CERT_PATH=C:\Users\home\.docker
 * DOCKER_TLS_VERIFY=1
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes={Main.class})
@EnablePerconaContainer(imageTag = "5.7.16.28", schema = "test_db", rootPassword = "testpass", exposeSchemaAs = "schema", exposeUrlAs = "url", exposePasswordAs = "password")
@TestExecutionListeners(value = PerconaContainerListener.class, mergeMode = MergeMode.MERGE_WITH_DEFAULTS)
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
