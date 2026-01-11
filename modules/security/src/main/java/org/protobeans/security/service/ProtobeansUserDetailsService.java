package org.protobeans.security.service;

import org.protobeans.security.model.AbstractProfile;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class ProtobeansUserDetailsService implements UserDetailsService {
    private ProfileService profileService;
    
    public ProtobeansUserDetailsService(ProfileService profileService) {
        this.profileService = profileService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AbstractProfile profile = profileService.getByLogin(username);

        if (profile == null) {
            throw new UsernameNotFoundException(username);
        }

        return profile;
    }
}
