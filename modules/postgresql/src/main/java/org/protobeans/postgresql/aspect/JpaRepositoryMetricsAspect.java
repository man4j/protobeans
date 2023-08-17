package org.protobeans.postgresql.aspect;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.util.concurrent.Striped;

import jakarta.annotation.PostConstruct;
import net.logstash.logback.marker.Markers;

@Aspect
public class JpaRepositoryMetricsAspect {
    private ThreadLocal<Boolean> flag = new ThreadLocal<>();
    
    private final ConcurrentHashMap<String, OperationProfile> operationsMap = new ConcurrentHashMap<>();
    
    private final String instance;
    
    private final String packageName;
    
    private volatile String logstashHost;
    
    private final Striped<Lock> striped = Striped.lock(4096);
    
    private static final Logger LOGSTASH_JSON_LOGGER = LoggerFactory.getLogger("LOGSTASH_JSON_LOGGER");
    
    public JpaRepositoryMetricsAspect(String packageName) {
        logstashHost = System.getenv("LOGSTASH_HOST");

        if (logstashHost == null || logstashHost.isBlank()) {
            logstashHost = System.getProperty("LOGSTASH_HOST");
        }

        instance = System.getenv("SERVICE_NAME") == null ? "default_instance" : System.getenv("SERVICE_NAME");
        this.packageName = packageName;
    }
    
    @PostConstruct
    public void init() {
        if (logstashHost != null && !logstashHost.isBlank()) {        
            Thread t = new Thread() {
                @Override
                public void run() {
                    while (!Thread.currentThread().isInterrupted()) {
                        for (String key : operationsMap.keySet()) {
                            OperationProfile profile = operationsMap.get(key);
                            
                            Map<String, Object> fields = new HashMap<>();
                            fields.put("protobeans.jpa.profiler.instance", instance);
                            fields.put("protobeans.jpa.profiler.operation", key);
                            fields.put("protobeans.jpa.profiler.totalTime", profile.getTotalTime());
                            fields.put("protobeans.jpa.profiler.count", profile.getCount());
                            fields.put("protobeans.jpa.profiler.metrics", true);//чтобы отличать метрики и логи
                            
                            LOGSTASH_JSON_LOGGER.info(Markers.appendEntries(fields), "");
                        }
                                                
                        try {
                            Thread.sleep(15_000);
                        } catch (@SuppressWarnings("unused") InterruptedException e) {
                            break;
                        }
                    }
                }
            };
            
            t.setDaemon(true);
            t.start();
        }
    }
    
    @Around("execution(* org.protobeans.postgresql.repository.ProtobeansJpaRepository+.*(..))")
    public Object logMetrics(ProceedingJoinPoint pjp) throws Throwable {
        if (flag.get() == null) {
            try {
                flag.set(true);
                
                long startTime = System.currentTimeMillis();
                
                Object result = pjp.proceed();
                
                String jpaMethod = pjp.getStaticPart().getSignature().getName();
                String stackTrace = generateStackTrace();
                
                String key = jpaMethod + "{\n" + stackTrace + "}";
                
                try {
                    striped.get(key).lock();
                    
                    OperationProfile prevProfile = operationsMap.get(key);
            
                    long opTime = System.currentTimeMillis() - startTime;
                    
                    OperationProfile profile;
                    
                    if (prevProfile == null) {
                        profile = new OperationProfile(opTime);
                    } else {
                        profile = new OperationProfile(prevProfile.getTotalTime() + opTime, prevProfile.getCount() + 1);
                    }
                    
                    operationsMap.put(key, profile);
                } finally {
                    striped.get(key).unlock();
                }

                return result;
            } finally {
                flag.remove();
            }
        }
        
        return pjp.proceed();
    }
    
    private String generateStackTrace() {
        String stackTrace = "";
        
        for (StackTraceElement e : Thread.currentThread().getStackTrace()) {
            if (e.toString().startsWith(packageName)
             && e.getLineNumber() != -1) {
                stackTrace += "  " + e.getClassName().substring(e.getClassName().lastIndexOf(".") + 1) + "." + e.getMethodName() + ":" + e.getLineNumber() + "\n";
            }
        }
        
        stackTrace = stackTrace.replace("EnhancerBySpringCGLIB", "");
        stackTrace = stackTrace.replace("FastClassBySpringCGLIB", "");
        
        return stackTrace;
    }
}
