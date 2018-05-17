package org.protobeans.mvcsecurity.example.controller;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.protobeans.mvc.config.MvcInitializer;
import org.protobeans.mvcsecurity.example.Main;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes=Main.class)
//без этой аннотации не работает, что она делает не совсем понятно. Вероятно спринг пытается создать контекст, 
//который не является WebApplicationContext
@WebAppConfiguration 
@DirtiesContext
public class SimpleControllerTest {
    private MockMvc mockMvc;
    
    @Before
    public void initMockMvc() {
        //WebApplicationContext knows which Servlet it is associated with (by having a link to the ServletContext)
        mockMvc = MockMvcBuilders.webAppContextSetup(MvcInitializer.rootApplicationContext)
                                 .apply(SecurityMockMvcConfigurers.springSecurity())
                                 .build();
    }
    
    @Test
    public void shouldWork() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/signin")).andExpect(MockMvcResultMatchers.status().isOk());
    }
    
    @Test
    public void shouldWorkWithRest() throws InterruptedException, ExecutionException, IOException {
        try (AsyncHttpClient client = new DefaultAsyncHttpClient()) {
            int statusCode = client.prepareGet("http://localhost:8080/signin").execute().get().getStatusCode();
            
            Assert.assertEquals(HttpStatus.OK.value(), statusCode);
        }
    }
}
