package org.protobeans.monitoring.config;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.protobeans.core.annotation.InjectFrom;
import org.protobeans.monitoring.annotation.EnableDockerMonitoring;
import org.protobeans.monitoring.model.MonitoringType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.context.annotation.Configuration;

import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.exceptions.DockerException;
import com.spotify.docker.client.messages.swarm.Service;
import com.spotify.docker.client.messages.swarm.Task;

import net.logstash.logback.marker.LogstashMarker;
import net.logstash.logback.marker.Markers;

@Configuration
@InjectFrom(EnableDockerMonitoring.class)
public class DockerMonitoringConfig {
    private static final Logger logger = LoggerFactory.getLogger(DockerMonitoringConfig.class);
    
    private int interval;
    
    @PostConstruct
    public void logStats() {
        new Thread() {
            @Override
            public void run() {
                @SuppressWarnings("resource")
                DockerClient docker = new DefaultDockerClient("unix:///var/run/docker.sock");
                
                MDC.put("monitoringType", MonitoringType.SWARM.name());
                
                while (!Thread.interrupted()) {
                    try {
                        List<Service> services = docker.listServices();
                        List<Task> tasks = docker.listTasks().stream().filter(t -> t.desiredState().equals("running") && t.status().state().equals("running")).collect(Collectors.toList());
                        
                        Map<String, List<Task>> taskMap = tasks.stream().collect(Collectors.groupingBy(Task::serviceId));
                        
                        for (Service service : services) {
                            int runningTasksCount = taskMap.getOrDefault(service.id(), Collections.emptyList()).size();
                            
                            LogstashMarker markers = Markers.append("swarm_service_id", service.id()).and(
                                                     Markers.append("swarm_service_name", service.spec().name())).and(
                                                     Markers.append("swarm_service_replicas", (service.spec().mode().replicated() == null || service.spec().mode().replicated().replicas() == null) ? runningTasksCount : service.spec().mode().replicated().replicas())).and(
                                                     Markers.append("swarm_service_running_tasks", runningTasksCount));
                            
                            if (service.spec().mode().replicated() != null && service.spec().mode().replicated().replicas() != null) {
                                long desiredReplicas = service.spec().mode().replicated().replicas();
                                long availableReplicas = runningTasksCount;
                                
                                if (desiredReplicas > availableReplicas) {
                                    markers.and(Markers.append("swarm_replica_difference", desiredReplicas - availableReplicas));
                                }
                            }
                            
                            logger.info(markers, "");
                        }
                        
                        Map<String, Service> serviceMap = services.stream().collect(Collectors.toMap(Service::id, s -> s));
                        
                        for (Task task : tasks) {
                            logger.info(Markers.append("swarm_task_id", task.id()).and(
                                        Markers.append("swarm_service_name", serviceMap.get(task.serviceId()).spec().name())).and(
                                        Markers.append("swarm_node_id", task.nodeId())), "");
                        }
                        
                        Thread.sleep(TimeUnit.SECONDS.toMillis(interval));
                    } catch (InterruptedException | DockerException e) {
                        logger.error("", e);
                        
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }.start();
    }
}
