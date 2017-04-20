package org.protobeans.monitoring.model;

import com.spotify.docker.client.messages.Container;
import com.spotify.docker.client.messages.swarm.Node;
import com.spotify.docker.client.messages.swarm.Service;
import com.spotify.docker.client.messages.swarm.Task;

public class ContainerInfo {
    private Container container;
    
    private Service service;
    
    private Task task;
    
    private Node node;
    
    private String monitoringPattern;

    public ContainerInfo(Container container, String monitoringPattern) {
        this.container = container;
        this.monitoringPattern = monitoringPattern;
    }
    
    public String getContainerName() {
        return container.names().toString();
    }

    public Container getContainer() {
        return container;
    }

    public Service getService() {
        return service;
    }
    
    public void setService(Service service) {
        this.service = service;
    }

    public Task getTask() {
        return task;
    }
    
    public void setTask(Task task) {
        this.task = task;
    }
    
    public String getMonitoringPattern() {
        return monitoringPattern;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }
}
