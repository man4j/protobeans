package org.protobeans.mvc.util;

public class ResourcesVersionBean {
    private String resourcesPath;
    
    private String resourcesUrl;

    private long lastModified;

    public ResourcesVersionBean(String resourcesPath, String resourcesUrl) {
        if (!resourcesPath.isEmpty() && !resourcesUrl.isEmpty()) {
            this.resourcesPath = PathUtils.dashedPath(resourcesPath);
            this.resourcesUrl = PathUtils.dashedPath(resourcesUrl);
            this.lastModified = FileUtils.getLastModified(this.resourcesPath);
        }
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
