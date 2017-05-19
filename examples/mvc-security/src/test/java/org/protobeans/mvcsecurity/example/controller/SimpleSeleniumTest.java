package org.protobeans.mvcsecurity.example.controller;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.protobeans.mvcsecurity.example.Main;
import org.protobeans.testcontainers.mysql.annotation.EnableMySqlContainer;
import org.protobeans.testcontainers.mysql.listener.MySqlContainerListener;
import org.protobeans.testcontainers.selenium.annotation.EnableSeleniumContainer;
import org.protobeans.testcontainers.selenium.annotation.WebDriver;
import org.protobeans.testcontainers.selenium.listener.SeleniumContainerListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestExecutionListeners.MergeMode;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes=Main.class)
@EnableSeleniumContainer(dockerHostSshKeyPath = "~/docker_host.ppk")
@EnableMySqlContainer(user = "test", password = "test", exposeUrlAs = "dbUrl", exposeSchemaAs = "dbSchema", exposeUserAs = "dbUser", exposePasswordAs = "dbPassword")
@TestExecutionListeners(value = {SeleniumContainerListener.class, MySqlContainerListener.class}, mergeMode = MergeMode.MERGE_WITH_DEFAULTS)
public class SimpleSeleniumTest {
    @Value("${webUrl}")
    private String webUrl;
    
    @WebDriver
    private RemoteWebDriver webDriver;
    
    private final static String LOGIN_FORM_TITLE_ID = "loginFormTitle";
    
    @Test
    public void shouldWork() {
        webDriver.get(webUrl + "/signin");

        WebElement loginFormTitle = webDriver.findElement(By.id(LOGIN_FORM_TITLE_ID));
        Assert.assertEquals("LOGIN FORM", loginFormTitle.getText());

        webDriver.findElement(By.id("localeLinkRu")).click();
        
        WebDriverWait wait = new WebDriverWait(webDriver, 30);
        wait.until(ExpectedConditions.stalenessOf(loginFormTitle));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(LOGIN_FORM_TITLE_ID)));
        
        loginFormTitle = webDriver.findElement(By.id(LOGIN_FORM_TITLE_ID));
        Assert.assertEquals("ФОРМА АВТОРИЗАЦИИ", loginFormTitle.getText());
    }
}
