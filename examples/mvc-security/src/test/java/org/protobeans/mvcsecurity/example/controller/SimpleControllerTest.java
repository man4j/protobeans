package org.protobeans.mvcsecurity.example.controller;

import java.util.concurrent.ExecutionException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.protobeans.mvc.config.DispatcherServletContextConfig;
import org.protobeans.mvcsecurity.example.Main;
import org.protobeans.testcontainers.mysql.annotation.EnablePerconaContainer;
import org.protobeans.testcontainers.mysql.listener.PerconaContainerListener;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestExecutionListeners.MergeMode;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.ning.http.client.AsyncHttpClient;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes=Main.class) 
@EnablePerconaContainer(imageTag = "5.7.16.17", schema = "test_db", rootPassword = "testpass", skipInit = true, exposeSchemaAs = "dbSchema", exposeUrlAs = "dbUrl", exposePasswordAs = "dbPassword")
@TestExecutionListeners(value = PerconaContainerListener.class, mergeMode = MergeMode.MERGE_WITH_DEFAULTS)
@DirtiesContext
public class SimpleControllerTest {
    private MockMvc mockMvc;
    
    @Before
    public void initMockMvc() {
        //WebApplicationContext knows which Servlet it is associated with (by having a link to the ServletContext)
        mockMvc = MockMvcBuilders.webAppContextSetup(DispatcherServletContextConfig.webApplicationContext)
                                 .apply(SecurityMockMvcConfigurers.springSecurity())
                                 .build();
    }
    
    @Test
    public void shouldWork() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/signin")).andExpect(MockMvcResultMatchers.status().isOk());
    }
    
    @Test
    public void shouldWorkWithRest() throws InterruptedException, ExecutionException {
        try (AsyncHttpClient client = new AsyncHttpClient()) {
            int statusCode = client.prepareGet("http://localhost:8080/signin").execute().get().getStatusCode();
            
            Assert.assertEquals(HttpStatus.OK.value(), statusCode);
        }
    }
}
