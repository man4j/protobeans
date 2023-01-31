package org.protobeans.faces.util;

import static com.sun.faces.RIConstants.ANNOTATED_CLASSES;

import java.lang.annotation.Annotation;
import java.net.URI;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;

import com.sun.faces.spi.AnnotationProvider;

public class ClassPathFacesAnnotationProvider extends AnnotationProvider {
    @Override
    public Map<Class<? extends Annotation>, Set<Class<?>>> getAnnotatedClasses(Set<URI> uris) {
        @SuppressWarnings("unchecked")
        Set<Class<? extends Annotation>> facesAnnotations = (Set<Class<? extends Annotation>>) servletContext.getAttribute(ANNOTATED_CLASSES);    
                
        Reflections reflections = new Reflections("com.equiron", "org.omnifaces");
        
        Map<Class<? extends Annotation>, Set<Class<?>>> map = new HashMap<>();
        
        facesAnnotations.forEach(annotation -> {        
            map.put(annotation, new HashSet<>(reflections.getTypesAnnotatedWith(annotation)));
        });
        
        return map;
    }
}
