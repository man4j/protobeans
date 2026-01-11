package org.protobeans.webapp.example.security;

import java.util.ArrayList;
import java.util.Collection;

import org.jspecify.annotations.Nullable;
import org.protobeans.security.model.AbstractProfile;
import org.protobeans.security.model.AuthenticationProviderWrapper;
import org.protobeans.security.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ott.OneTimeTokenAuthenticationProvider;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.FactorGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class TokenAuthenticationProviderWrapper implements AuthenticationProviderWrapper {
    @Autowired ProfileService profileService;
    
    @Autowired TokenService ottService;
    
    @Override
    public AuthenticationProvider getProvider() {
        return new OneTimeTokenAuthenticationProvider(ottService, new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                AbstractProfile profile = profileService.getByLogin(username);

                if (profile == null) {
                    throw new UsernameNotFoundException(username);
                }
                
                var grantedAuthorities = new ArrayList<>(profile.getAuthorities());
                
                var auth = SecurityContextHolder.getContext().getAuthentication();

                var passwordAuthrority = auth.getAuthorities().stream()
                                             .map(GrantedAuthority::getAuthority)
                                             .filter(a -> a.equals(FactorGrantedAuthority.PASSWORD_AUTHORITY))
                                             .findAny()
                                             .isPresent();
                
                if (passwordAuthrority) {
                    grantedAuthorities.add(FactorGrantedAuthority.fromAuthority(FactorGrantedAuthority.PASSWORD_AUTHORITY));
                }
                
                return new UserDetails() {
                    @Override
                    public String getUsername() {
                        return username;
                    }
                    
                    @Override
                    public @Nullable String getPassword() {
                        return null;
                    }
                    
                    @Override
                    public Collection<? extends GrantedAuthority> getAuthorities() {
                        return grantedAuthorities;
                    }
                };
            }
        });
    }
}
