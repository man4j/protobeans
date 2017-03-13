package org.protobeans.security.model;

import java.util.Locale;

import org.hibernate.validator.constraints.NotBlank;
import org.protobeans.security.validation.SignIn;

@SignIn
public class SignInForm {
    @NotBlank(message = "{form.email.empty}")
    private String email;

    @NotBlank(message = "{form.password.empty}")
    private String password;

    private boolean rememberMe;

    public String getEmail() {
        return email == null ? null : email.toLowerCase(Locale.US);
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(boolean rememberMe) {
        this.rememberMe = rememberMe;
    }
}
