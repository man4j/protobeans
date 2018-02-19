package org.protobeans.security.model;

import java.util.Set;

public interface AbstractProfile {
    Set<String> getRoles();
    
    String getEmail();

    String getPassword();

    String getConfirmUuid();
    
    boolean isConfirmed();
}
