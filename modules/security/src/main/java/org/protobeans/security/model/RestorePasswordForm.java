package org.protobeans.security.model;

import org.protobeans.security.validation.LoginExists;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestorePasswordForm {
    @NotBlank(message = "{form.login.empty}")
    @LoginExists
    private String login;
}
