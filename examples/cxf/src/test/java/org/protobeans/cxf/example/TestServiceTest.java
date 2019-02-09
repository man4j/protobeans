package org.protobeans.cxf.example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.protobeans.cxf.example.model.TestRequest;
import org.protobeans.cxf.example.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes=Main.class)
@WebAppConfiguration 
@DirtiesContext
public class TestServiceTest {
    @Autowired
    private TestService testService;
    
    @Test
    public void shouldWork() throws Exception {
        Assertions.assertNotNull(testService.test(new TestRequest("data")));
    }
}
