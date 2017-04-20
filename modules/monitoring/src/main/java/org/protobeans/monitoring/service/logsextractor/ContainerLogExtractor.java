package org.protobeans.monitoring.service.logsextractor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import org.protobeans.monitoring.model.ContainerInfo;
import org.protobeans.monitoring.model.MonitoringType;
import org.protobeans.monitoring.model.logsextractor.ContainerLogMessage;
import org.protobeans.monitoring.model.logsextractor.ContainerLogMessage.LogChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.DockerClient.LogsParam;

import net.logstash.logback.marker.Markers;

public class ContainerLogExtractor {
    private static final Logger logger = LoggerFactory.getLogger(ContainerLogExtractor.class);
    
    private Executor executor = Executors.newCachedThreadPool();
    
    private DockerClient docker;
    
    private ConcurrentHashMap<String, Boolean> attachedContainers = new ConcurrentHashMap<>();
    
    public ContainerLogExtractor(DockerClient docker) {
        this.docker = docker;
    }

    @SuppressWarnings("resource")
    public void readLines(ContainerInfo containerInfo, Consumer<ContainerLogMessage> lineConsumer) {
        if (!attachedContainers.containsKey(containerInfo.getContainer().id())) {
            attachedContainers.put(containerInfo.getContainer().id(), true);

            final PipedInputStream stdout = new PipedInputStream(65_536);
            final PipedInputStream stderr = new PipedInputStream(65_536);
            
            attachToLogs(containerInfo, stdout, stderr, lineConsumer);
            
            readFromStream(containerInfo, stdout, LogChannel.STDOUT, lineConsumer);
            readFromStream(containerInfo, stderr, LogChannel.STDERR, lineConsumer);
        }
    }
    
    @SuppressWarnings("resource")
    private void attachToLogs(ContainerInfo containerInfo, PipedInputStream stdout, PipedInputStream stderr, Consumer<ContainerLogMessage> lineConsumer) {
        executor.execute(() -> {
            MDC.put("monitoringType", MonitoringType.LOGSEXTRACTOR.name());
            
            try {
                logger.info("Attach to: " + containerInfo.getContainerName());
                
                docker.logs(containerInfo.getContainer().id(), LogsParam.stderr(), LogsParam.stdout(), LogsParam.follow(), LogsParam.tail(0))
                      .attach(new PipedOutputStream(stdout), new PipedOutputStream(stderr));
            } catch (Exception e) {
                logger.error("Exception while extracting logs from task: " + containerInfo.getContainerName(), e);
            } finally {
                logger.info("Detach from: " + containerInfo.getContainerName());
                
                attachedContainers.remove(containerInfo.getContainer().id());
                
                lineConsumer.accept(null);
            }
        });
    }
    
    private void readFromStream(ContainerInfo containerInfo, final PipedInputStream in, LogChannel logChannel, Consumer<ContainerLogMessage> lineConsumer) {
        executor.execute(() -> {
            MDC.put("monitoringType", MonitoringType.LOGS.name());
            
            try (BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
                while (!Thread.interrupted()) {
                    String line;
                    
                    try {
                        line = br.readLine();
                    } catch (IOException e) {
                        if (attachedContainers.containsKey(containerInfo.getContainer().id())) {//возможно еще не приаттачились
                            Thread.sleep(1_000);
                            
                            continue;
                        }
                        
                        throw e;
                    }
                    
                    if (line == null) break;
                    
                    lineConsumer.accept(new ContainerLogMessage(containerInfo, line, logChannel));
                }
            } catch (Exception e) {
                logger.error(Markers.append("monitoringType", MonitoringType.LOGSEXTRACTOR), "Exception while extracting logs from task: " + containerInfo.getContainerName(), e);
            } finally {
                logger.info(Markers.append("monitoringType", MonitoringType.LOGSEXTRACTOR), "Close " + logChannel.name() + " log stream from: " + containerInfo.getContainerName());
                
                lineConsumer.accept(null);
            }
        });
    }
}
