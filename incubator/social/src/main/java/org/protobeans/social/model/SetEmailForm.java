package org.protobeans.social.model;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

public class SetEmailForm {
    @NotBlank(message = "{form.email.empty}")
    @Email(message = "{form.email.format}")
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
