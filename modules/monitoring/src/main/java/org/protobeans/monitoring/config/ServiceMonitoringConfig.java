package org.protobeans.monitoring.config;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.protobeans.core.annotation.InjectFrom;
import org.protobeans.monitoring.annotation.EnableServiceMonitoring;
import org.protobeans.monitoring.model.MonitoringType;
import org.protobeans.monitoring.service.StatusChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.messages.swarm.NetworkAttachment;
import com.spotify.docker.client.messages.swarm.Node;
import com.spotify.docker.client.messages.swarm.Task;

import net.logstash.logback.marker.Markers;

@Configuration
@InjectFrom(EnableServiceMonitoring.class)
@ComponentScan(basePackageClasses = StatusChecker.class)
public class ServiceMonitoringConfig {
    public static final int CHECK_INTERVAL = 15;
    
    private static final Logger logger = LoggerFactory.getLogger(ServiceMonitoringConfig.class);
    
    private String dnsAddr;
    
    @Autowired
    private List<StatusChecker> checkers = new ArrayList<>();
    
    @PostConstruct
    public void logStats() {
        new Thread() {
            @Override
            public void run() {
                @SuppressWarnings("resource")
                DockerClient docker = new DefaultDockerClient("unix:///var/run/docker.sock");
                
                java.security.Security.setProperty("networkaddress.cache.ttl" , "1");
                
                while (!Thread.interrupted()) {
                    for (String dns : dnsAddr.split(",")) {
                        try {
                            List<Task> runningTasks = docker.listTasks().stream().filter(t -> t.desiredState().equals("running") && t.status().state().equals("running"))
                                                                                 .collect(Collectors.toList());
                            
                            Map<String, Node> nodeMap = docker.listNodes().stream().collect(Collectors.toMap(Node::id, n -> n));
                            
                            Map<String, Task> ip2Task = new HashMap<>();
                            
                            for (Task t : runningTasks) {
                                if (t.networkAttachments() != null) {
                                    for (NetworkAttachment na : t.networkAttachments()) {
                                        for (String ip : na.addresses()) {
                                            ip2Task.put(ip.split("/")[0], t);
                                        }
                                    }
                                }
                            }
                        
                            String protocol = dns.split(":")[0];
                            String host = dns.split(":")[1];
                            int port = Integer.parseInt(dns.split(":")[2]);
                        
                            InetAddress[] resolvedIps = InetAddress.getAllByName("tasks." + host);
                            
                            logger.info(Markers.append("monitoringType", MonitoringType.JAVA_AGENT.name()), "Resolved IP addresses for " + host + " :" + Arrays.stream(resolvedIps).map(InetAddress::getHostAddress).collect(Collectors.toList()));
                            
                            for (InetAddress resolvedIp : resolvedIps) {
                                for (StatusChecker checker : checkers) {
                                    try {
                                        if (protocol.equalsIgnoreCase(checker.getMonitoringProtocol().name()) && ip2Task.containsKey(resolvedIp.getHostAddress())) {
                                            Task t = ip2Task.get(resolvedIp.getHostAddress());
                                            Node n = nodeMap.get(t.nodeId());
                                            
                                            checker.checkStatus(resolvedIp.getHostAddress(), port, t, n);
                                        }
                                    } catch (Exception e) {
                                        logger.error(Markers.append("monitoringType", MonitoringType.JAVA_AGENT.name()), "", e);
                                    }
                                }
                            }
                        } catch (Exception e) {
                            logger.error(Markers.append("monitoringType", MonitoringType.JAVA_AGENT.name()), "", e);
                        }
                    }
                    
                    try {
                        Thread.sleep(TimeUnit.SECONDS.toMillis(CHECK_INTERVAL));
                    } catch (InterruptedException e) {
                        logger.error(Markers.append("monitoringType", MonitoringType.JAVA_AGENT.name()), "", e);
                        
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }.start();
    }
}
