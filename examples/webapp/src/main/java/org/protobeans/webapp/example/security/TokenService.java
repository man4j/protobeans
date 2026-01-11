package org.protobeans.webapp.example.security;

import java.time.Instant;
import java.util.UUID;

import org.protobeans.webapp.example.service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ott.DefaultOneTimeToken;
import org.springframework.security.authentication.ott.GenerateOneTimeTokenRequest;
import org.springframework.security.authentication.ott.OneTimeToken;
import org.springframework.security.authentication.ott.OneTimeTokenAuthenticationToken;
import org.springframework.security.authentication.ott.OneTimeTokenService;
import org.springframework.stereotype.Service;

@Service
public class TokenService implements OneTimeTokenService {
    @Autowired UserProfileService profileService;
    
    @Override
    public OneTimeToken generate(GenerateOneTimeTokenRequest request) {
        var userProfile = profileService.getByLogin(request.getUsername());
        userProfile.setConfirmUuid(UUID.randomUUID().toString());
        profileService.update(userProfile);
        
        return new DefaultOneTimeToken(userProfile.getConfirmUuid(), request.getUsername(), Instant.MAX);
    }

    @Override
    public OneTimeToken consume(OneTimeTokenAuthenticationToken authenticationToken) {
        var userProfile = profileService.getByToken(authenticationToken.getTokenValue());
        
        if (userProfile == null) {
            return null;
        }
        
        userProfile.setConfirmUuid(UUID.randomUUID().toString());
        userProfile.setConfirmed(true);
        profileService.update(userProfile);
        
        return new DefaultOneTimeToken(authenticationToken.getTokenValue(), userProfile.getEmail(), Instant.MAX);
    }
}
