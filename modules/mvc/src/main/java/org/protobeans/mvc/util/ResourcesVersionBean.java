package org.protobeans.mvc.util;

public class ResourcesVersionBean {
    private String resourcesPath;
    
    private String resourcesUrl;

    private long lastModified;

    public ResourcesVersionBean(String resourcesPath, String resourcesUrl) {
        if (!resourcesUrl.endsWith("/")) {
            resourcesUrl += "/";
        }
        
        if (!resourcesUrl.startsWith("/")) {
            resourcesUrl = "/" + resourcesUrl;
        }
        
        if (!resourcesPath.endsWith("/")) {
            resourcesPath += "/";
        }
        
        if (!resourcesPath.startsWith("/")) {
            resourcesPath = "/" + resourcesPath;
        }
        
        this.resourcesPath = resourcesPath;
        this.resourcesUrl = resourcesUrl;
        this.lastModified = FileUtils.getLastModified(resourcesPath);
    }
    
    public String getResourcesPath() {
        return resourcesPath;
    }
    
    public String getResourcesUrl() {
        return resourcesUrl;
    }
    
    public long getLastModified() {
        return lastModified;
    }
}
