package org.protobeans.social.service;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class ProtobeansSocialUserDetailsService implements SocialUserDetailsService {
    public static final String NOT_EXISTING_USER_ID_ATTRIBUTE_NAME = "notExistingUserId";
    
    @Autowired
    private UserDetailsService userDetailsService;
    
    @Autowired
    private HttpSession session;
    
    @Override
    public SocialUserDetails loadUserByUserId(String userId) throws UsernameNotFoundException {
        try {
            return (SocialUserDetails) userDetailsService.loadUserByUsername(userId);
        } catch (UsernameNotFoundException e) {
            session.setAttribute(NOT_EXISTING_USER_ID_ATTRIBUTE_NAME, userId);
            
            throw e;
        }
    }
}
