package org.protobeans.mvc.example.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.protobeans.mvc.config.DispatcherServletContextConfig;
import org.protobeans.mvc.example.Main;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@WebAppConfiguration("classpath:/")
@ContextHierarchy({
    @ContextConfiguration(classes=Main.class),//parent web application context 
    @ContextConfiguration(classes=DispatcherServletContextConfig.class)//child web application context
})
public class SimpleControllerTest {
    @Autowired
    private WebApplicationContext wac;
    
    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;
    
    @Before
    public void initMockMvc() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }
    
    @Test
    public void shouldWork() throws Exception {
        String expectedStringResult = "hello";
        String expectedJsonResult = objectMapper.writeValueAsString(expectedStringResult);
        
        mockMvc.perform(MockMvcRequestBuilders.get("/hello").accept(MediaType.APPLICATION_JSON))
                                                            .andExpect(MockMvcResultMatchers.content().json(expectedJsonResult))
                                                            .andExpect(MockMvcResultMatchers.status().isOk());
        
        mockMvc.perform(MockMvcRequestBuilders.get("/hello").accept(MediaType.TEXT_PLAIN))
                                                            .andExpect(MockMvcResultMatchers.content().string(expectedStringResult))
                                                            .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
