package org.protobeans.mvc.util;

public class PathUtils {
    public static String dashedPath(String resourcesPath) {
        if (!resourcesPath.endsWith("/")) {
            resourcesPath += "/";
        }
        
        if (!resourcesPath.startsWith("/")) {
            resourcesPath = "/" + resourcesPath;
        }
        
        return resourcesPath;
    }
}
