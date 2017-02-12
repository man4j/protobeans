package org.protobeans.security.util;

public class SecurityUrlsBean {
    private String[] ignoreUrls;
    
    private String loginUrl;

    public SecurityUrlsBean(String[] ignoreUrls, String loginUrl) {
        this.ignoreUrls = ignoreUrls;
        this.loginUrl = loginUrl;
    }

    public String[] getIgnoreUrls() {
        return ignoreUrls;
    }

    public String getLoginUrl() {
        return loginUrl;
    }
}
