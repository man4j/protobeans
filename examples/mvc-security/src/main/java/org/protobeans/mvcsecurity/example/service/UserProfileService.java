package org.protobeans.mvcsecurity.example.service;

import java.util.Arrays;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.protobeans.hibernate.annotation.WithTransaction;
import org.protobeans.mvcsecurity.example.dao.UserProfileDao;
import org.protobeans.mvcsecurity.example.model.UserProfile;
import org.protobeans.security.model.AbstractProfile;
import org.protobeans.security.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserProfileService implements ProfileService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private UserProfileDao userProfileDao;
    
    @PersistenceContext
    private EntityManager em;
    
    @WithTransaction(readOnly=true)
    @Override
    public UserProfile getByEmail(String email) {
        return em.createQuery("SELECT u FROM UserProfile u WHERE u.email = :email", UserProfile.class)
                 .setParameter("email", email)
                 .getResultStream().findFirst().orElse(null);
    }

    @WithTransaction
    @Override
    public UserProfile update(AbstractProfile profile) {
        return userProfileDao.update((UserProfile) profile);
    }
    
    @WithTransaction
    public void updatePassword(String email, String newPassword) {
        em.createQuery("UPDATE UserProfile SET password = :newPassword WHERE email = :email")
          .setParameter("newPassword", passwordEncoder.encode(newPassword))
          .setParameter("email", email)
          .executeUpdate();
    }

    @WithTransaction
    public UserProfile createAndSave(String email, String password, String userName, boolean isConfirmed, String... roles) {
        UserProfile p = new UserProfile(UUID.randomUUID().toString());
        
        p.setEmail(email);
        p.setPassword(passwordEncoder.encode(password));
        p.setUserName(userName);
        p.setConfirmUuid(UUID.randomUUID().toString());
        p.setConfirmed(isConfirmed);
        p.getRoles().addAll(Arrays.asList(roles));
        
        userProfileDao.insert(p);
        
        return p;
    }
}
