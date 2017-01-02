package org.protobeans.scheduler.example;

import org.protobeans.core.EntryPoint;
import org.protobeans.scheduler.annotation.EnableScheduler;
import org.springframework.scheduling.annotation.Scheduled;

@EnableScheduler(poolSize = 10)
public class Main {
    
    @Scheduled(fixedDelay = 1_000)
    void scheduledMethod() {
        System.out.println(Thread.currentThread().getName());
    }
    
    public static void main(String[] args) {
        EntryPoint.run(Main.class);
    }
}
