package org.protobeans.async.config;

import javax.annotation.PostConstruct;

import org.protobeans.async.annotation.EnableAsync;
import org.protobeans.core.annotation.InjectFrom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@InjectFrom(EnableAsync.class)
public class AsyncConfig {
    private int corePoolSize;
    
    private int maxPoolSize = Integer.MAX_VALUE;
    
    private boolean interruptOnClose;
    
    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    
    @PostConstruct
    public void init() {
        threadPoolTaskExecutor.setCorePoolSize(corePoolSize);
        threadPoolTaskExecutor.setMaxPoolSize(maxPoolSize);
        threadPoolTaskExecutor.setWaitForTasksToCompleteOnShutdown(!interruptOnClose);
        
        threadPoolTaskExecutor.initialize();
    }
}
