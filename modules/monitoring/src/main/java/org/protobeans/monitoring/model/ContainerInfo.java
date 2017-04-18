package org.protobeans.monitoring.model;

import com.spotify.docker.client.messages.Container;
import com.spotify.docker.client.messages.swarm.Service;
import com.spotify.docker.client.messages.swarm.Task;

public class ContainerInfo {
    private Container container;
    
    private Service service;
    
    private Task task;

    public ContainerInfo(Container container) {
        this.container = container;
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
}
