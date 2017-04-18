package org.protobeans.monitoring.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.protobeans.monitoring.model.ContainerInfo;

import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.DockerClient.ListContainersParam;
import com.spotify.docker.client.exceptions.DockerException;
import com.spotify.docker.client.messages.Container;
import com.spotify.docker.client.messages.swarm.Service;
import com.spotify.docker.client.messages.swarm.Task;

public class ContainersUtils {
    private DockerClient docker;
    
    public ContainersUtils(DockerClient docker) {
        this.docker = docker;
    }
    
    public List<ContainerInfo> findContainersForService(String... serviceNamePatterns) throws DockerException, InterruptedException {
        Map<String, Service> serviceMap = docker.listServices().stream().collect(Collectors.toMap(Service::id, s -> s));
        Map<String, Task> container2Task = docker.listTasks().stream().filter(t -> t.desiredState().equals("running") && t.status().state().equals("running")).collect(Collectors.toMap(t -> t.status().containerStatus().containerId(), t -> t));

        List<Container> detectedContainers = docker.listContainers(ListContainersParam.allContainers());
        
        List<ContainerInfo> infos = new ArrayList<>();
        
        for (Container container : detectedContainers) {
            Task containerTask = container2Task.get(container.id());
            
            if (containerTask != null) {
                Service containerService = serviceMap.get(containerTask.serviceId());
                
                if (containerService != null) {
                    if (isServiceIncluded(containerService, serviceNamePatterns)) {
                        ContainerInfo info = new ContainerInfo(container);
                        
                        info.setService(containerService);
                        info.setTask(containerTask);
                        
                        infos.add(info);
                    }
                }
            }
        }
        
        return infos;
    }
    
    private boolean isServiceIncluded(Service service, String... patterns) {
        for (String pattern : patterns) {
           if (service.spec().name().contains(pattern)) {
               return true;
           }
        }
        
        return false;
    }
}
