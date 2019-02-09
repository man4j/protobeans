package org.protobeans.cxf.example;

import java.util.Collections;

import javax.annotation.PostConstruct;
import javax.validation.Validation;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Endpoint;
import javax.xml.ws.soap.SOAPBinding;

import org.apache.cxf.annotations.SchemaValidation.SchemaValidationType;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.ext.logging.LoggingFeature;
import org.apache.cxf.feature.FastInfosetFeature;
import org.apache.cxf.feature.validation.DefaultSchemaValidationTypeProvider;
import org.apache.cxf.feature.validation.SchemaValidationFeature;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.transport.common.gzip.GZIPFeature;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import org.apache.cxf.validation.BeanValidationFeature;
import org.apache.cxf.validation.BeanValidationProvider;
import org.protobeans.cxf.annotation.EnableCxf;
import org.protobeans.cxf.example.service.TestService;
import org.protobeans.cxf.example.service.TestServiceImpl;
import org.protobeans.cxf.example.util.ExceptionLoggingInterceptor;
import org.protobeans.mvc.MvcEntryPoint;
import org.protobeans.undertow.annotation.EnableUndertow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.google.common.collect.Lists;

@EnableCxf
@EnableUndertow
@Configuration
@ComponentScan(basePackageClasses = TestServiceImpl.class)
public class Main {    
    @Autowired
    private SpringBus bus;

    @Autowired(required = false)
    private LocalValidatorFactoryBean validatorFactoryBean;

    @PostConstruct
    public void configureBus() {
        BeanValidationFeature bvf = new BeanValidationFeature();
        
        if (validatorFactoryBean != null) {
            bvf.setProvider(new BeanValidationProvider(validatorFactoryBean));
        } else {
            bvf.setProvider(new BeanValidationProvider(Validation.buildDefaultValidatorFactory()));
        }
        
        bus.getOutFaultInterceptors().add(new ExceptionLoggingInterceptor());

        bus.getFeatures().addAll(Lists.newArrayList(new SchemaValidationFeature(new DefaultSchemaValidationTypeProvider(Collections.singletonMap("*", SchemaValidationType.IN))),
                                                    new GZIPFeature(),
                                                    new FastInfosetFeature(),
                                                    new LoggingFeature(),
                                                    bvf));
        bus.initialize();
    }

    @Bean
    public Endpoint testEndpoint(TestServiceImpl testServiceImpl) {
        EndpointImpl endpoint = new EndpointImpl(bus, testServiceImpl);

        endpoint.publish("/testService");
        
        SOAPBinding binding = (SOAPBinding) endpoint.getBinding();
        
        binding.setMTOMEnabled(true);
        
        return endpoint;
    }
    
    @SuppressWarnings("resource")
    @Bean
    public TestService testService() {
        JaxWsProxyFactoryBean proxyFactory = new JaxWsProxyFactoryBean();
        proxyFactory.setServiceClass(TestService.class);
        proxyFactory.setAddress("http://127.0.0.1:8080/ws/testService");
        proxyFactory.setBus(new SpringBus());
        proxyFactory.getFeatures().addAll(Lists.newArrayList(new SchemaValidationFeature(new DefaultSchemaValidationTypeProvider(Collections.singletonMap("*", SchemaValidationType.RESPONSE))),
                                                             new LoggingFeature()));
        
        TestService service = proxyFactory.create(TestService.class);
        
        ((BindingProvider)service).getRequestContext().put(BindingProvider.SESSION_MAINTAIN_PROPERTY, true);

        Client proxy = ClientProxy.getClient(service);
        
        HTTPConduit http = (HTTPConduit) proxy.getConduit();
        
        HTTPClientPolicy httpClientPolicy = new HTTPClientPolicy();
                 
        httpClientPolicy.setConnectionTimeout(30000);
        httpClientPolicy.setReceiveTimeout(30000);
         
        http.setClient(httpClientPolicy);
        
        return service;
    }
    
    public static void main(String[] args) {
        MvcEntryPoint.run(Main.class);
    }
}