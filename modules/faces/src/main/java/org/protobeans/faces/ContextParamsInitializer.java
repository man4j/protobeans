package org.protobeans.faces;

import java.util.EnumSet;
import java.util.Set;

import org.jboss.weld.environment.servlet.Container;
import org.jboss.weld.environment.servlet.Listener;
import org.jboss.weld.environment.undertow.UndertowContainer;
import org.omnifaces.ApplicationListener;
import org.omnifaces.filter.GzipResponseFilter;
import org.protobeans.faces.config.ProtobeansFacesConfig;

import jakarta.faces.validator.BeanValidator;
import jakarta.servlet.DispatcherType;
import jakarta.servlet.ServletContainerInitializer;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.validation.ValidatorFactory;

public class ContextParamsInitializer implements ServletContainerInitializer {
    @SuppressWarnings("resource")
    @Override
    public void onStartup(Set<Class<?>> c, ServletContext ctx) throws ServletException {
        ctx.setInitParameter("primefaces.THEME", "rain-victoria-dim");
        ctx.setInitParameter("primefaces.MOVE_SCRIPTS_TO_BOTTOM", "true");
        ctx.setInitParameter("primefaces.SUBMIT", "partial");
        ctx.setInitParameter("primefaces.CLIENT_SIDE_VALIDATION", "true");
        ctx.setInitParameter("primefaces.MULTI_VIEW_STATE_STORE", "client-window");
        ctx.setInitParameter("primefaces.TRANSFORM_METADATA", "true");
        ctx.setInitParameter("primefaces.FONT_AWESOME", "true");
        
        ctx.setInitParameter("jakarta.faces.FACELETS_LIBRARIES", "/primefaces-rain.taglib.xml");
        ctx.setInitParameter("jakarta.faces.INTERPRET_EMPTY_STRING_SUBMITTED_VALUES_AS_NULL", "true");
        ctx.setInitParameter("jakarta.faces.FACELETS_REFRESH_PERIOD", "0");
        ctx.setInitParameter("jakarta.faces.validator.ENABLE_VALIDATE_WHOLE_BEAN", "true");
        ctx.setInitParameter("jakarta.faces.PROJECT_STAGE", "Development");
        ctx.setInitParameter("jakarta.faces.DISABLE_FACESSERVLET_TO_XHTML", "false");
        ctx.setInitParameter("jakarta.faces.FACELETS_SUFFIX", ".xhtml");
        ctx.setInitParameter("jakarta.faces.DEFAULT_SUFFIX", ".xhtml");
        ctx.setInitParameter("jakarta.faces.FACELETS_SKIP_COMMENTS", "true");
        ctx.setInitParameter("javax.faces.FACELETS_SKIP_COMMENTS", "true");

        ctx.setInitParameter("org.omnifaces.VERSIONED_RESOURCE_HANDLER_VERSION", (System.currentTimeMillis() / 1_000) + "");
        ctx.setAttribute(BeanValidator.VALIDATOR_FACTORY_KEY, ProtobeansFacesConfig.springContext.getBean(ValidatorFactory.class));

        //CDI
        ctx.setInitParameter(Container.CONTEXT_PARAM_CONTAINER_CLASS, UndertowContainer.class.getName());
        ctx.addListener(Listener.class);
        
        //Для инициализации OmniFaces нужен не только initializer, но и этот listener
        ctx.addListener(ApplicationListener.class);

        ctx.addFilter("gzipResponseFilter", GzipResponseFilter.class)
           .addMappingForServletNames(EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD), true, "FacesServlet");
    }
}