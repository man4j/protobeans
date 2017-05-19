package org.protobeans.hibernate.mysql.example.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.protobeans.hibernate.mysql.example.Main;
import org.protobeans.testcontainers.mysql.annotation.EnableMySqlContainer;
import org.protobeans.testcontainers.mysql.listener.MySqlContainerListener;
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
@EnableMySqlContainer(user = "test", password = "test", exposeUrlAs = "url", exposeSchemaAs = "schema", exposeUserAs = "user", exposePasswordAs = "password")
@TestExecutionListeners(value = MySqlContainerListener.class, mergeMode = MergeMode.MERGE_WITH_DEFAULTS)
public class UserServiceTest {
    @Autowired
    private UserService userService;
    
    @Test
    public void shouldInsertUsers() {
        Assert.assertEquals(0, userService.count());
        
        userService.insertUsers(10);
        
        Assert.assertEquals(10, userService.count());
    }
}
