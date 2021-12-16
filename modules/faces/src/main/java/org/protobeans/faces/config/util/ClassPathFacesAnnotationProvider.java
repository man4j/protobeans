package org.protobeans.faces.config.util;

import com.sun.faces.spi.AnnotationProvider;
import jakarta.faces.convert.FacesConverter;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.sun.faces.RIConstants.ANNOTATED_CLASSES;

public class ClassPathFacesAnnotationProvider extends AnnotationProvider {
    @Override
    public Map<Class<? extends Annotation>, Set<Class<?>>> getAnnotatedClasses(Set<URI> uris) {
        @SuppressWarnings("unchecked")
        Set<Class<? extends Annotation>> facesAnnotations = (Set<Class<? extends Annotation>>) servletContext.getAttribute(ANNOTATED_CLASSES);    
                
        Reflections reflections = new Reflections("com.equiron", "org.omnifaces");
        
        Map<Class<? extends Annotation>, Set<Class<?>>> map = new HashMap<>();
        
        facesAnnotations.forEach(annotation -> {        
            map.put(annotation, reflections.getTypesAnnotatedWith(annotation));
        });
        
        return map;
    }
    
    public static void main(String[] args) {
        Reflections reflections = new Reflections("com.equiron");
        
        System.out.println(reflections.getTypesAnnotatedWith(FacesConverter.class));
    }
}
