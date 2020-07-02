package org.protobeans.scheduler.config;

import org.protobeans.core.annotation.InjectFrom;
import org.protobeans.scheduler.annotation.EnableScheduler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

@Configuration
@EnableScheduling
@InjectFrom(EnableScheduler.class)
public class SchedulerConfig implements SchedulingConfigurer {
    private String poolSize;
    
    private boolean interruptOnClose;
    
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setTaskScheduler(threadPoolTaskScheduler());
    }

    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        
        scheduler.setPoolSize(Integer.parseInt(poolSize));
        scheduler.setAwaitTerminationSeconds(Integer.MAX_VALUE);
        scheduler.setRemoveOnCancelPolicy(true);
        scheduler.setWaitForTasksToCompleteOnShutdown(!interruptOnClose);
        scheduler.setThreadNamePrefix("ProtoBeansTaskScheduler-");
        
        return scheduler;
    }
}
