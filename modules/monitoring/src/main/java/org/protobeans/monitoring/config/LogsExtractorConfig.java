package org.protobeans.monitoring.config;

import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.protobeans.core.annotation.InjectFrom;
import org.protobeans.monitoring.annotation.EnableLogsExtractor;
import org.protobeans.monitoring.model.ContainerInfo;
import org.protobeans.monitoring.model.MonitoringType;
import org.protobeans.monitoring.service.ContainersUtils;
import org.protobeans.monitoring.service.logsextractor.ContainerLogExtractor;
import org.protobeans.monitoring.service.logsextractor.LogProcessingChain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.exceptions.DockerException;

@Configuration
@InjectFrom(EnableLogsExtractor.class)
public class LogsExtractorConfig {
    private DefaultDockerClient docker = new DefaultDockerClient("unix:///var/run/docker.sock");

    private static final Logger logger = LoggerFactory.getLogger(LogsExtractorConfig.class);
    
    private String patterns;
    
    private ConcurrentHashMap<String, Boolean> processedContainers = new ConcurrentHashMap<>();
    
    private ContainersUtils containersUtils = new ContainersUtils(docker);
    
    private ContainerLogExtractor logExtractor = new ContainerLogExtractor(docker);
    
    @Autowired
    private LogProcessingChain logProcessingChain;

    @PostConstruct
    public void extractLogs() {
        new Thread() {
            @Override
            public void run() {
                MDC.put("monitoringType", MonitoringType.LOGSEXTRACTOR.name());
                
                logger.info("Enabled patterns: " + patterns);
                
                while (!Thread.interrupted()) {
                    try {
                        for (ContainerInfo containerInfo : containersUtils.findContainersForService(patterns.split(","))) {
                            if (!processedContainers.containsKey(containerInfo.getContainer().id())) {
                                processedContainers.put(containerInfo.getContainer().id(), true);
                                        
                                logExtractor.readLines(containerInfo, logMessage -> {
                                    if (logMessage == null) {
                                        processedContainers.remove(containerInfo.getContainer().id());
                                    } else {
                                        logProcessingChain.getFirst().processLogMessage(logMessage);
                                    }
                                });
                            }
                        }
                        
                        Thread.sleep(15_000);
                    } catch (InterruptedException | DockerException e) {
                        logger.error("", e);
                        
                        Thread.currentThread().interrupt();
                    }
                } 
            }
        }.start();
    }
}
