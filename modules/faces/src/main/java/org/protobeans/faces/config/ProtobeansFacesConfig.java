package org.protobeans.faces.config;

import com.sun.faces.config.FacesInitializer;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.BeanValidator;
import jakarta.servlet.DispatcherType;
import jakarta.servlet.ServletContainerInitializer;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.MessageInterpolator;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.jboss.weld.environment.servlet.EnhancedListener;
import org.omnifaces.ApplicationInitializer;
import org.omnifaces.ApplicationListener;
import org.omnifaces.filter.GzipResponseFilter;
import org.protobeans.core.annotation.InjectFrom;
import org.protobeans.faces.config.annotation.EnableFaces;
import org.protobeans.undertow.annotation.EnableUndertow;
import org.protobeans.undertow.annotation.Initializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.MessageSourceResourceBundleLocator;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.EnumSet;
import java.util.Locale;
import java.util.Set;

@Configuration
@InjectFrom(EnableFaces.class)
@ComponentScan("org.protobeans.faces.config.util")
@EnableUndertow(resourcesPath = "META-INF/resources", initializers = {@Initializer(initializer = ContextParamsInitializer.class),
                                                                      @Initializer(initializer = FacesInitializer.class),
                                                                      @Initializer(initializer = ApplicationInitializer.class),
                                                                      @Initializer(initializer = EnhancedListener.class)})
public class ProtobeansFacesConfig {
    private static Logger logger = LoggerFactory.getLogger(ProtobeansFacesConfig.class);
    
    public static volatile ApplicationContext springContext;
    
    @Autowired
    private ApplicationContext ctx;
    
    @PostConstruct
    public void init() {
        springContext = ctx;
    }
    
    @Bean
    public ValidatorFactory validatorFactory() {
        return Validation.byDefaultProvider()
                         .configure()
                         .messageInterpolator(new FacesMessageInterpolator(new ResourceBundleMessageInterpolator(new MessageSourceResourceBundleLocator(messageSource()), isCached())))
                         .buildValidatorFactory();
    }
    
    @Bean
    public Validator validator(ValidatorFactory validatorFactory) {
        return validatorFactory.getValidator();
    }
    
    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        
        messageSource.setBasenames("classpath:messages/messages");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setCacheSeconds(isCached() ? -1 : 0);
        messageSource.setFallbackToSystemLocale(false);
        
        return messageSource;
    }
    
    private boolean isCached() {
        boolean isCached = true;
        
        if (System.getProperty("os.name").toLowerCase().contains("windows") || "false".equals(System.getProperty("cacheMessages"))) {
            isCached = false;
        }
        
        return isCached;
    }
}
class ContextParamsInitializer implements ServletContainerInitializer {
    @SuppressWarnings("resource")
    @Override
    public void onStartup(Set<Class<?>> c, ServletContext ctx) throws ServletException {
        ctx.setInitParameter("primefaces.THEME", "nova-light");
        ctx.setInitParameter("primefaces.MOVE_SCRIPTS_TO_BOTTOM", "true");
        ctx.setInitParameter("primefaces.SUBMIT", "partial");
        ctx.setInitParameter("primefaces.CLIENT_SIDE_VALIDATION", "true");
        ctx.setInitParameter("primefaces.MULTI_VIEW_STATE_STORE", "client-window");
        ctx.setInitParameter("primefaces.TRANSFORM_METADATA", "true");
        
        ctx.setInitParameter("jakarta.faces.INTERPRET_EMPTY_STRING_SUBMITTED_VALUES_AS_NULL", "true");
        ctx.setInitParameter("jakarta.faces.FACELETS_REFRESH_PERIOD", "0");
        ctx.setInitParameter("jakarta.faces.validator.ENABLE_VALIDATE_WHOLE_BEAN", "true");
        ctx.setInitParameter("jakarta.faces.PROJECT_STAGE", "Production");
        ctx.setInitParameter("jakarta.faces.DISABLE_FACESSERVLET_TO_XHTML", "true");
        ctx.setInitParameter("jakarta.faces.FACELETS_SUFFIX", ".jsf");
        ctx.setInitParameter("jakarta.faces.DEFAULT_SUFFIX", ".jsf");
        
        ctx.setInitParameter("org.omnifaces.SOCKET_ENDPOINT_ENABLED", "true");
        ctx.setInitParameter("org.omnifaces.VERSIONED_RESOURCE_HANDLER_VERSION", (System.currentTimeMillis() / 1_000) + "");
        
        ctx.setInitParameter("org.jboss.weld.context.mapping", ".*\\.jsf");
        
        ctx.setAttribute(BeanValidator.VALIDATOR_FACTORY_KEY, ProtobeansFacesConfig.springContext.getBean(ValidatorFactory.class));
        
        ctx.addServlet("welcome", new HttpServlet() {
            @Override
            protected void service(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
                if (req.getRequestURI().equals("/")) {
                    res.sendRedirect("/index.jsf");
                } else {
                    super.service(req, res);
                }
            }
        }).addMapping("/");
        
        //Для инициализации OmniFaces нужен не только initializer, но и этот listener
        ctx.addListener(ApplicationListener.class);
        
        ctx.addFilter("gzipResponseFilter", GzipResponseFilter.class)
           .addMappingForServletNames(EnumSet.of(DispatcherType.REQUEST), true, "FacesServlet");
    }
}

class FacesMessageInterpolator implements MessageInterpolator {
    private final MessageInterpolator delegate;
    
    public FacesMessageInterpolator(MessageInterpolator delegate) {
        this.delegate = delegate;
    }
    
    @Override
    public String interpolate(String messageTemplate, Context context) {
        Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
        return delegate.interpolate(messageTemplate, context, locale);
    }
    
    @Override
    public String interpolate(String messageTemplate, Context context, Locale locale) {
        return delegate.interpolate(messageTemplate, context, locale);
    }
}
