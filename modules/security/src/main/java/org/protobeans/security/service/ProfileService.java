package org.protobeans.security.service;

import org.protobeans.security.model.AbstractProfile;

public interface ProfileService {
    AbstractProfile getByLogin(String login);
    
    AbstractProfile update(AbstractProfile profile);
}
