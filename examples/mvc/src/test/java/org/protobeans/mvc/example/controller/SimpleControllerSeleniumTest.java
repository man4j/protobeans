package org.protobeans.mvc.example.controller;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.protobeans.mvc.example.Main;
import org.protobeans.testcontainers.selenium.annotation.EnableSeleniumContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes=Main.class)
@EnableSeleniumContainer(dockerHostSshKeyPath = "~/docker_host.ppk")
public class SimpleControllerSeleniumTest {
    @Autowired
    private WebDriver webDriver;
    
    @Value("${webUrl}")
    private String webUrl;
    
    @Test
    public void shouldWork() {
        webDriver.get(webUrl + "/hello");
        
        Assert.assertEquals("hello", webDriver.findElement(By.tagName("body")).getText());
    }
}
