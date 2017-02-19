package org.protobeans.mvcsecurity.example.service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.protobeans.mvcsecurity.example.model.UserProfile;
import org.protobeans.security.model.AbstractProfile;
import org.protobeans.security.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class InMemoryProfileService implements ProfileService {
    @Autowired
    private ShaPasswordEncoder passwordEncoder;

    private Map<String, UserProfile> byEmail = new ConcurrentHashMap<>();
    private Map<String, UserProfile> byUuid = new ConcurrentHashMap<>();
    
    @PostConstruct
    public void init() {
        UserProfile p = new UserProfile();
        
        p.setConfirmed(true);
        p.setEmail("admin@ya.ru");
        p.setPassword(passwordEncoder.encodePassword("123456", p.getEmail()));
        p.setUserName("Admin");
        p.getRoles().add("ROLE_ADMIN");
        
        byEmail.put(p.getEmail(), p);
    }

    @Override
    public UserProfile getByEmail(String email) {
        return byEmail.get(email);
    }

    @Override
    public UserProfile getByConfirmUuid(String uuid) {
        throw new UnsupportedOperationException();
    }

    @Override
    public UserProfile update(AbstractProfile profile) {
        throw new UnsupportedOperationException();
    }

    public UserProfile create(String email, String password, String userName) {
        UserProfile p = new UserProfile();
        
        p.setEmail(email);
        p.setPassword(passwordEncoder.encodePassword("123456", password));
        p.setUserName(userName);
        p.setConfirmUuid(UUID.randomUUID().toString());
        
        byEmail.put(p.getEmail(), p);
        byUuid.put(p.getConfirmUuid(), p);
        
        return p;
    }
}
