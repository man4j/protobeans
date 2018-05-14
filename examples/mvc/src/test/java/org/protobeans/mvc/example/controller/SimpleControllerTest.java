package org.protobeans.mvc.example.controller;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.protobeans.mvc.config.MvcInitializer;
import org.protobeans.mvc.example.Main;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.undertow.util.Headers;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes=Main.class)
@WebAppConfiguration
public class SimpleControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    
    private MockMvc mockMvc;
    
    @Before
    public void initMockMvc() {
        //WebApplicationContext knows which Servlet it is associated with (by having a link to the ServletContext)
        mockMvc = MockMvcBuilders.webAppContextSetup(MvcInitializer.rootApplicationContext).build();
    }
    
    @Test
    public void shouldWork() throws Exception {
        String expectedStringResult = "Hello, Masha!";
        String expectedJsonResult = objectMapper.writeValueAsString(expectedStringResult);
        
        mockMvc.perform(MockMvcRequestBuilders.get("/echo?param=Masha")
                                              .accept(MediaType.APPLICATION_JSON))
                                              .andExpect(MockMvcResultMatchers.content().json(expectedJsonResult))
                                              .andExpect(MockMvcResultMatchers.status().isOk());
        
        mockMvc.perform(MockMvcRequestBuilders.get("/echo?param=Masha")
                                              .accept(MediaType.TEXT_PLAIN))
                                              .andExpect(MockMvcResultMatchers.content().string(expectedStringResult))
                                              .andExpect(MockMvcResultMatchers.status().isOk());
    }
    
    @Test
    public void shouldWorkWithRest() throws IOException, InterruptedException, ExecutionException {
        String expectedStringResult = "Hello, Masha!";
        String expectedJsonResult = objectMapper.writeValueAsString(expectedStringResult);
        
        try (AsyncHttpClient client = new DefaultAsyncHttpClient()) {
            String response = client.prepareGet("http://localhost:8080/echo?param=Masha").addHeader(Headers.ACCEPT_STRING, MediaType.APPLICATION_JSON_VALUE)
                                                                                        .execute().get().getResponseBody();
            
            Assert.assertEquals(expectedJsonResult, response);
            
            response = client.prepareGet("http://localhost:8080/echo?param=Masha").addHeader(Headers.ACCEPT_STRING, MediaType.TEXT_PLAIN_VALUE)
                                                                                 .execute().get().getResponseBody();

            Assert.assertEquals(expectedStringResult, response);
        }
    }
}
