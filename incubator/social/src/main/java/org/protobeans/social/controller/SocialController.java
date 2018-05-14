package org.protobeans.social.controller;

import org.protobeans.security.annotation.PermitAll;
import org.springframework.stereotype.Controller;

@PermitAll("/auth/*") //for SocialAuthenticationFilter
@Controller
public class SocialController {
    //empty
}
