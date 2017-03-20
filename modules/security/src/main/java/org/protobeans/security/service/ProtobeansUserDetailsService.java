package org.protobeans.security.service;

import java.util.stream.Collectors;

import org.protobeans.security.model.AbstractProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.social.security.SocialUser;
import org.springframework.stereotype.Service;

/**
 * This service used by Spring Social and Remember Me auth. 
 */
@Service
public class ProtobeansUserDetailsService implements UserDetailsService {
    @Autowired
    private ProfileService profileService;
    
    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        AbstractProfile profile = profileService.getByEmail(id);

        if (profile == null) {
            throw new UsernameNotFoundException(id);
        }

        return new SocialUser(id, profile.getPassword(), profile.isConfirmed(), true, true, true, profile.getRoles().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
    }
}
