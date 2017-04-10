package org.protobeans.monitoring.config;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
                java.security.Security.setProperty("networkaddress.cache.ttl" , "1");
                
                while (!Thread.interrupted()) {
                    for (String dns : dnsAddr.split(",")) {
                        String protocol = dns.split(":")[0];
                        String host = dns.split(":")[1];
                        int port = Integer.parseInt(dns.split(":")[2]);
                        
                        try {
                            InetAddress[] resolvedIps = InetAddress.getAllByName(host);
                            
                            logger.info(Markers.append("monitoringType", MonitoringType.COMMON.name()), "Resolved IP addresses for " + host + " :" + Arrays.stream(resolvedIps).map(InetAddress::getHostAddress).collect(Collectors.toList()));
                            
                            for (InetAddress resolvedIp : resolvedIps) {
                                for (StatusChecker checker : checkers) {
                                    try {
                                        if (protocol.equalsIgnoreCase(checker.getMonitoringProtocol().name())) {
                                            checker.checkStatus(resolvedIp.getHostAddress(), port);
                                        }
                                    } catch (Exception e) {
                                        logger.error(Markers.append("monitoringType", checker.getMonitoringType().name()), "", e);
                                    }
                                }
                            }
                        } catch (Exception e) {
                            logger.error(Markers.append("monitoringType", MonitoringType.COMMON.name()), "", e);
                        }
                    }
                    
                    try {
                        Thread.sleep(TimeUnit.SECONDS.toMillis(CHECK_INTERVAL));
                    } catch (InterruptedException e) {
                        logger.error(Markers.append("monitoringType", MonitoringType.COMMON.name()), "", e);
                        
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }.start();
    }
}
