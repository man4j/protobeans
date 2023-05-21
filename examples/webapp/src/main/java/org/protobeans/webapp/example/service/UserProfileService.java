package org.protobeans.webapp.example.service;

import java.util.Arrays;
import java.util.UUID;

import org.protobeans.postgresql.annotation.WithTransaction;
import org.protobeans.security.model.AbstractProfile;
import org.protobeans.security.service.ProfileService;
import org.protobeans.webapp.example.entity.UserProfile;
import org.protobeans.webapp.example.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserProfileService implements ProfileService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private UserProfileRepository userProfileRepository;
    
    @WithTransaction(readOnly=true)
    @Override
    public UserProfile getByLogin(String login) {
        return userProfileRepository.findByEmail(login);
    }

    @WithTransaction
    @Override
    public UserProfile update(AbstractProfile profile) {
        return userProfileRepository.save((UserProfile) profile);
    }
    
    @WithTransaction
    public void updatePassword(String login, String newPassword) {
        UserProfile p = userProfileRepository.findByEmail(login);
        p.setPassword(passwordEncoder.encode(newPassword));
    }

    @WithTransaction
    public UserProfile createAndSave(String login, String password, String userName, boolean isConfirmed, String... roles) {
        UserProfile p = new UserProfile();
        
        p.setEmail(login);
        p.setPassword(passwordEncoder.encode(password));
        p.setUserName(userName);
        p.setConfirmUuid(UUID.randomUUID().toString());
        p.setConfirmed(isConfirmed);
        p.getRoles().addAll(Arrays.asList(roles));
        
        userProfileRepository.save(p);
        
        return p;
    }
}
