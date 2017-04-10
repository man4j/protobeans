package org.protobeans.monitoring.example;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.protobeans.core.EntryPoint;
import org.protobeans.monitoring.annotation.EnableJavaMonitoring;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import net.logstash.logback.marker.Markers;

@EnableJavaMonitoring
public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    
    @PostConstruct
    public void logGenerator() {
        new Thread() {
            @Override
            public void run() {
                MDC.put("monitoringType", "JAVA_SERVICE");
                
                try {
                    MDC.put("java_hostname", InetAddress.getLocalHost().getHostName());
                } catch (IllegalArgumentException | UnknownHostException e) {
                    logger.error("", e);
                }
                
                while (!Thread.interrupted()) {
                    int randomInt = new Random().nextInt(100);
                    
                    logger.info(Markers.append("randomInt", randomInt), "randomInt: " + randomInt);
                    
                    if (randomInt % 2 == 0) {
                        logger.error("", new IllegalStateException("Bad number: " + randomInt));
                    }
                    
                    try {
                        Thread.sleep(TimeUnit.SECONDS.toMillis(1));
                    } catch (@SuppressWarnings("unused") InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }.start();
    }
    
    public static void main(String[] args) {
        EntryPoint.run(Main.class);
    }
}