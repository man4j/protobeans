package org.protobeans.webapp.example.model;

import org.protobeans.webapp.example.validation.SignInOtt;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@SignInOtt
public class SignInOttForm {
    @NotBlank(message = "{form.token.empty}")
    private String token;
}
