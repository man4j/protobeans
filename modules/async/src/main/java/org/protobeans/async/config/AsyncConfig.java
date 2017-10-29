package org.protobeans.async.config;

import java.util.concurrent.ThreadPoolExecutor;

import org.protobeans.async.annotation.EnableAsync;
import org.protobeans.core.annotation.InjectFrom;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@org.springframework.scheduling.annotation.EnableAsync(proxyTargetClass = true)
@InjectFrom(EnableAsync.class)//TODO: Injection not work (из за особенностей конфигурации EnableAsync)
public class AsyncConfig implements AsyncConfigurer {
    private int corePoolSize;
    
    private int maxPoolSize = Integer.MAX_VALUE;
    
    private boolean interruptOnClose;
    
    @Override
    public ThreadPoolTaskExecutor getAsyncExecutor() {
        return threadPoolTaskExecutor();
    }

    @Bean
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(0);
        executor.setWaitForTasksToCompleteOnShutdown(!interruptOnClose);
        executor.setAwaitTerminationSeconds(Integer.MAX_VALUE);
        executor.setThreadNamePrefix("ProtobeansAsyncExecutor-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return null;
    }
}
