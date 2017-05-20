package org.protobeans.testcontainers.selenium.listener;

import java.util.Random;

import org.openqa.selenium.remote.DesiredCapabilities;
import org.protobeans.testcontainers.selenium.annotation.EnableSeleniumContainer;
import org.protobeans.testcontainers.selenium.annotation.WebDriver;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;
import org.springframework.util.ReflectionUtils;
import org.testcontainers.containers.BrowserWebDriverContainer;
import org.testcontainers.containers.BrowserWebDriverContainer.VncRecordingMode;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class SeleniumContainerListener extends AbstractTestExecutionListener {
    private BrowserWebDriverContainer<?> browser;
    
    private Session session;
    
    @Override
    @SuppressWarnings({ "rawtypes", "resource" })
    public void beforeTestClass(TestContext testContext) throws Exception {
        EnableSeleniumContainer annotation = testContext.getTestClass().getAnnotation(EnableSeleniumContainer.class);
        
        String webUrl = "http://" + annotation.dockerHostIp() + ":" + annotation.webPort();
        
        if (System.getenv("DOCKER_HOST") != null) {
            String fullDockerHostPath = System.getenv("DOCKER_HOST");
            String dockerHost = fullDockerHostPath.substring(fullDockerHostPath.indexOf("//") + 2, fullDockerHostPath.lastIndexOf(':'));
    
            JSch jsch = new JSch();
            JSch.setConfig("StrictHostKeyChecking", "no");
            
            jsch.addIdentity(annotation.dockerHostSshKeyPath());
            
            session = jsch.getSession(annotation.dockerHostSshUser(), dockerHost);

            session.connect();
            session.setServerAliveInterval(30000);

            int port = new Random().nextInt(10_000) + 20_000;
            
            for (int i = 0; i <= 10; i++) {
                try {
                    session.setPortForwardingR("0.0.0.0", port, "localhost", annotation.webPort());
                    break;
                } catch (JSchException e) {
                    if (i == 10) {
                        throw new RuntimeException(e);
                    }
                    
                    port = new Random().nextInt(10_000) + 20_000;
                }
            }
            
            webUrl = "http://" + annotation.dockerHostIp() + ":" + port;
        }
        
        System.setProperty("webUrl", webUrl);
        
        browser = new BrowserWebDriverContainer().withDesiredCapabilities(DesiredCapabilities.firefox())
                                                 .withRecordingMode(VncRecordingMode.SKIP, null);
        
        browser.start();
        
        System.out.println("VNC URL: " + browser.getVncAddress());
    }
    
    @Override
    public void prepareTestInstance(TestContext testContext) throws Exception {
        ReflectionUtils.doWithFields(testContext.getTestClass(), f -> {
          if (f.getAnnotation(WebDriver.class) != null) {
              f.setAccessible(true);
              f.set(testContext.getTestInstance(), browser.getWebDriver());
          }
      });
    }

    @Override
    public void afterTestClass(TestContext testContext) throws Exception {
        browser.getWebDriver().quit();
        browser.stop();
    }
}
