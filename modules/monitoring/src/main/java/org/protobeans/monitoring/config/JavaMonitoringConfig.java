package org.protobeans.monitoring.config;

import java.lang.management.ClassLoadingMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.ThreadMXBean;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.protobeans.core.annotation.InjectFrom;
import org.protobeans.monitoring.annotation.EnableJavaMonitoring;
import org.protobeans.monitoring.model.MonitoringType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
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
                
                MDC.put("monitoringType", MonitoringType.JAVA.name());
                try {
                    MDC.put("java_hostname", InetAddress.getLocalHost().getHostName());
                } catch (IllegalArgumentException | UnknownHostException e) {
                    logger.error("", e);
                    
                    return;
                }
                
                while (!Thread.interrupted()) {
                    try {
                        logger.info(Markers.append("java_loadedClassCount", classLoadingMXBean.getLoadedClassCount()).and(
                                    Markers.append("java_unloadedClassCount", classLoadingMXBean.getUnloadedClassCount())).and(
                                    Markers.append("java_usedMemory", memoryMXBean.getHeapMemoryUsage().getUsed())).and(
                                    Markers.append("java_committedMemory", memoryMXBean.getHeapMemoryUsage().getCommitted())).and(
                                    Markers.append("java_maxMemory", memoryMXBean.getHeapMemoryUsage().getMax())).and(
                                    Markers.append("java_nonHeapMemory", memoryMXBean.getNonHeapMemoryUsage().getUsed())).and(
                                    Markers.append("java_osArch", operatingSystemMXBean.getArch())).and(
                                    Markers.append("java_availableProcessors", operatingSystemMXBean.getAvailableProcessors())).and(
                                    Markers.append("java_threadCount", threadMXBean.getThreadCount())), "");
                        
                        Thread.sleep(TimeUnit.SECONDS.toMillis(interval));
                    } catch (@SuppressWarnings("unused") InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }.start();
    }
}
