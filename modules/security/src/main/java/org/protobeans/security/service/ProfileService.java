package org.protobeans.security.service;

import org.protobeans.security.model.AbstractProfile;

public interface ProfileService {
    AbstractProfile getByEmail(String email);
    
    AbstractProfile getByConfirmUuid(String uuid);

    AbstractProfile update(AbstractProfile profile);

    AbstractProfile create(String email, String password, boolean confirmed);
}
