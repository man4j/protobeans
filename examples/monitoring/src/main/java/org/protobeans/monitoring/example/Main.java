package org.protobeans.monitoring.example;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.protobeans.core.EntryPoint;
import org.protobeans.monitoring.annotation.EnableJavaMonitoring;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.logstash.logback.marker.Markers;

@EnableJavaMonitoring
public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    
    @PostConstruct
    public void logGenerator() {
        new Thread() {
            @Override
            public void run() {
                while (!Thread.interrupted()) {
                    int randomInt = new Random().nextInt(100);
                    
                    Map<String, Object> metrics = new HashMap<>();
                    
                    metrics.put("imagenarium.metrics", true);
                    metrics.put("my_java_service.randomInt", randomInt);
                    
                    logger.info(Markers.appendEntries(metrics), "");
                    
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