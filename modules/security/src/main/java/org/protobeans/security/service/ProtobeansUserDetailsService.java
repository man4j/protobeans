package org.protobeans.security.service;

import org.protobeans.security.model.AbstractProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ProtobeansUserDetailsService implements UserDetailsService {
    @Autowired
    @Lazy
    private ProfileService profileService;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AbstractProfile profile = profileService.getByLogin(username);

        if (profile == null) {
            throw new UsernameNotFoundException(username);
        }

        return profile;
    }
}
