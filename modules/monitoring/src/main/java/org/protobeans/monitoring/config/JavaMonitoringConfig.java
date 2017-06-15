package org.protobeans.monitoring.config;

import java.lang.management.ClassLoadingMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.ThreadMXBean;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.protobeans.core.annotation.InjectFrom;
import org.protobeans.monitoring.annotation.EnableJavaMonitoring;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import net.logstash.logback.marker.Markers;

@Configuration
@InjectFrom(EnableJavaMonitoring.class)
public class JavaMonitoringConfig {
    private static final Logger logger = LoggerFactory.getLogger(JavaMonitoringConfig.class);
    
    private int interval;
    
    @PostConstruct
    public void logStats() {
        new Thread() {
            @Override
            public void run() {
                ClassLoadingMXBean classLoadingMXBean = ManagementFactory.getClassLoadingMXBean();
                MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
                OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
                ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
                
                while (!Thread.interrupted()) {
                    try {
                        Map<String, Object> metrics = new HashMap<>();
                        
                        metrics.put("imagenarium.metrics", true);
                        metrics.put("java_loadedClassCount", classLoadingMXBean.getLoadedClassCount());
                        metrics.put("java_unloadedClassCount", classLoadingMXBean.getUnloadedClassCount());
                        metrics.put("java_usedMemory", memoryMXBean.getHeapMemoryUsage().getUsed());
                        metrics.put("java_committedMemory", memoryMXBean.getHeapMemoryUsage().getCommitted());
                        metrics.put("java_maxMemory", memoryMXBean.getHeapMemoryUsage().getMax());
                        metrics.put("java_nonHeapMemory", memoryMXBean.getNonHeapMemoryUsage().getUsed());
                        metrics.put("java_osArch", operatingSystemMXBean.getArch());
                        metrics.put("java_availableProcessors", operatingSystemMXBean.getAvailableProcessors());
                        metrics.put("java_threadCount", threadMXBean.getThreadCount());
                        
                        logger.info(Markers.appendEntries(metrics), "");
                        
                        Thread.sleep(TimeUnit.SECONDS.toMillis(interval));
                    } catch (@SuppressWarnings("unused") InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }.start();
    }
}
