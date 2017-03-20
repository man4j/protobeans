package org.protobeans.security.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

public class SocialLoginUrlAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint {
    private String linkFormUrl;
    
    public SocialLoginUrlAuthenticationEntryPoint(String loginFormUrl, String linkFormUrl) {
        super(loginFormUrl);
        
        this.linkFormUrl = linkFormUrl;
    }
    
    @Override
    protected String determineUrlToUseForThisRequest(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) {
        
        
        
        return super.determineUrlToUseForThisRequest(request, response, exception);
    }
}
