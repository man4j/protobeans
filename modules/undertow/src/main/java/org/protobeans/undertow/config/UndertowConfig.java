package org.protobeans.undertow.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.lang3.ArrayUtils;
import org.protobeans.core.annotation.InjectFrom;
import org.protobeans.undertow.annotation.EnableUndertow;
import org.protobeans.undertow.annotation.Initializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;

import io.undertow.Undertow;
import io.undertow.Undertow.Builder;
import io.undertow.predicate.Predicate;
import io.undertow.predicate.Predicates;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.encoding.ContentEncodingRepository;
import io.undertow.server.handlers.encoding.EncodingHandler;
import io.undertow.server.handlers.encoding.GzipEncodingProvider;
import io.undertow.server.handlers.resource.ClassPathResourceManager;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;
import io.undertow.servlet.api.MimeMapping;
import io.undertow.servlet.api.ServletContainerInitializerInfo;
import io.undertow.websockets.jsr.WebSocketDeploymentInfo;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.HandlesTypes;

@Configuration
@InjectFrom(EnableUndertow.class)
public class UndertowConfig {
    protected DeploymentInfo deploymentInfo = Servlets.deployment();
    
    private String host;
    
    private String port;
    
    private Initializer[] initializers;
    
    private Initializer[] userInitializers;
    
    private Undertow undertow;
    
    private String errorPage;
    
    private int sessionTimeout;

    private String workerThreads;
    
    private String ioThreads;
    
    protected Builder configure() throws ServletException {
        deploymentInfo.setContextPath("/")
                      .setDeploymentName("app.war")
                      .setClassLoader(this.getClass().getClassLoader())
                      .setDefaultSessionTimeout(sessionTimeout)
                      .addMimeMapping(new MimeMapping("jsf", "application/xhtml+xml"))
                      .setResourceManager(new ClassPathResourceManager(this.getClass().getClassLoader(), "META-INF/resources"));
        
        if (!errorPage.isEmpty()) {
            deploymentInfo.addErrorPage(Servlets.errorPage(errorPage));
        }
        
        deploymentInfo.getServletContextAttributes().put(WebSocketDeploymentInfo.ATTRIBUTE_NAME, new WebSocketDeploymentInfo());
        
        Initializer[] resultInitializers = ArrayUtils.addAll(initializers, userInitializers);
        
        for (Initializer initializer : resultInitializers) {
        	Set<Class<?>> handlesTypes = new HashSet<>(Arrays.asList(initializer.handleTypes()));
        	
        	if (handlesTypes.isEmpty()) {
        		HandlesTypes annotation = initializer.initializer().getAnnotation(HandlesTypes.class);
        		
        		if (annotation != null) {
        			handlesTypes = Set.of(annotation.value());
        		}
        	}
        	
            deploymentInfo.addServletContainerInitializer(new ServletContainerInitializerInfo(initializer.initializer(), handlesTypes));
        }
        
        final EncodingHandler encodingHandler = new EncodingHandler(new ContentEncodingRepository().addEncodingHandler("gzip", 
                new GzipEncodingProvider(8), 50, Predicates.and(Predicates.requestLargerThan(1024), 
                                                               new CompressibleMimeTypePredicate("text/html",
                                                                                                 "text/xml",
                                                                                                 "text/plain",
                                                                                                 "text/css",
                                                                                                 "text/javascript",
                                                                                                 "application/javascript",
                                                                                                 "application/json"))))
                                                           .setNext(createServletDeploymentHandler());
        
        Builder builder = Undertow.builder().addHttpListener(Integer.parseInt(port), host)
                                            .setHandler(encodingHandler);
        
        int iWorkerThreads = Integer.parseInt(workerThreads);
        int iIoThreads = Integer.parseInt(ioThreads);
        
        if (iWorkerThreads > 0) {
            System.out.println("Worker threads: " + iWorkerThreads);
            builder.setWorkerThreads(iWorkerThreads);
        }
        
        if (iIoThreads > 0) {
            System.out.println("IO threads: " + iIoThreads);
            builder.setIoThreads(iIoThreads);
        }
        
        return builder;
    }
    
    private HttpHandler createServletDeploymentHandler() throws ServletException {
        DeploymentManager manager = Servlets.defaultContainer().addDeployment(deploymentInfo);

        manager.deploy();
        
        return manager.start();
    }
    
    @PostConstruct
    public void start() throws ServletException {
        undertow = configure().build();
        undertow.start();
    }
    
    @PreDestroy
    public void stop() {
        undertow.stop();
    }
    
    private static class CompressibleMimeTypePredicate implements Predicate {
        private final List<MimeType> mimeTypes;

        public CompressibleMimeTypePredicate(String... mimeTypes) {
            this.mimeTypes = new ArrayList<>(mimeTypes.length);
            for (String mimeTypeString : mimeTypes) {
                this.mimeTypes.add(MimeTypeUtils.parseMimeType(mimeTypeString));
            }
        }

        @Override
        public boolean resolve(HttpServerExchange value) {
            String contentType = value.getResponseHeaders().getFirst("Content-Type");
            
            if (contentType != null) {
                for (MimeType mimeType : this.mimeTypes) {
                    if (mimeType.isCompatibleWith(MimeTypeUtils.parseMimeType(contentType))) {
                        return true;
                    }
                }
            }
            
            return false;
        }
    }
}
