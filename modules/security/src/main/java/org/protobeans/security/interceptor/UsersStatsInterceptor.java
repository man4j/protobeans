package org.protobeans.security.interceptor;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;

import org.protobeans.mvc.interceptor.OperationProfile;
import org.protobeans.security.service.SecurityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.google.common.cache.CacheBuilder;
import com.google.common.util.concurrent.Striped;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.logstash.logback.marker.Markers;

public class UsersStatsInterceptor implements HandlerInterceptor {
    @Autowired
    private SecurityService securityService;
    
    private ThreadLocal<Long> startTime = new ThreadLocal<>();
    
    private final Map<String, OperationProfile> usersMap = CacheBuilder.newBuilder().expireAfterWrite(Duration.ofMinutes(1)).<String, OperationProfile>build().asMap();
    
    private final String instance;
    
    private volatile String logstashHost;
    
    private final Striped<Lock> striped = Striped.lock(4096);
    
    private static final Logger LOGSTASH_JSON_LOGGER = LoggerFactory.getLogger("LOGSTASH_JSON_LOGGER");
    
    public UsersStatsInterceptor() {
        logstashHost = System.getenv("LOGSTASH_HOST");

        if (logstashHost == null || logstashHost.isBlank()) {
            logstashHost = System.getProperty("LOGSTASH_HOST");
        }

        instance = System.getenv("SERVICE_NAME") == null ? "default_instance" : System.getenv("SERVICE_NAME");
    }
    
    @PostConstruct
    public void init() {
        if (logstashHost != null && !logstashHost.isBlank()) {        
            Thread t = new Thread() {
                @Override
                public void run() {
                    while (!Thread.currentThread().isInterrupted()) {
                        for (String key : usersMap.keySet()) {
                            OperationProfile profile = usersMap.get(key);
                            
                            Map<String, Object> fields = new HashMap<>();
                            fields.put("protobeans.users.profiler.instance", instance);
                            fields.put("protobeans.users.profiler.operation", key);
                            fields.put("protobeans.users.profiler.totalTime", profile.getTotalTime());
                            fields.put("protobeans.users.profiler.count", profile.getCount());
                            fields.put("protobeans.users.profiler.metrics", true);//чтобы отличать метрики и логи
                            
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

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        startTime.set(System.currentTimeMillis());
        return true;
    }
    
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (handler instanceof HandlerMethod m) {
            if (securityService.getCurrentUser() != null) {
                if (!m.getMethod().getReturnType().equals(SseEmitter.class)) {
                    long diff = System.currentTimeMillis() - startTime.get();
                    
                    String login = securityService.getCurrentUser().getLogin();
                    
                    try {
                        striped.get(login).lock();
                        
                        OperationProfile prevProfile = usersMap.get(login);
                
                        OperationProfile profile;
                        
                        if (prevProfile == null) {
                            profile = new OperationProfile(diff);
                        } else {
                            profile = new OperationProfile(prevProfile.getTotalTime() + diff, prevProfile.getCount() + 1);
                        }
                        
                        usersMap.put(login, profile);
                    } finally {
                        striped.get(login).unlock();
                    }
                }
            }
        }
    }
}

