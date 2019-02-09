package org.protobeans.webapp.example.service;

//import java.io.Serializable;
//
//import org.springframework.security.access.PermissionEvaluator;
//import org.springframework.security.core.Authentication;
//import org.springframework.stereotype.Component;

//@Component
//public class MyPermissionEvaluator implements PermissionEvaluator {
//    @Override
//    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
//        if (targetDomainObject instanceof String) {
//            String messageId = (String) targetDomainObject;
//            
//            if (messageId.equals("1")) return true;
//        }
//        
//        return false;
//    }
//
//    @Override
//    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
//        return false;
//    }
//}
