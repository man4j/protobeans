package org.protobeans.security.model;

import org.protobeans.security.validation.LoginExists;

import jakarta.validation.constraints.NotBlank;

public class RestorePasswordForm {
    @NotBlank(message = "{form.login.empty}")
    @LoginExists
    private String login;

    public String getLogin() {
        return login;
    }
    
    public void setLogin(String login) {
        this.login = login;
    }
}
