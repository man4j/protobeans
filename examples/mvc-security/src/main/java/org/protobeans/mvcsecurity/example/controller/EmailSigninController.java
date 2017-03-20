package org.protobeans.mvcsecurity.example.controller;

import java.util.UUID;

import org.protobeans.security.annotation.Anonymous;
import org.protobeans.security.model.AbstractProfile;
import org.protobeans.security.service.ProfileService;
import org.protobeans.security.service.SecurityService;
import org.protobeans.social.service.SocialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/email_signin")
@Anonymous("/email_signin")
public class EmailSigninController {
    @Autowired
    private ProfileService profileService;

    @Autowired
    private SecurityService securityService;
    
    @Autowired
    private SocialService socialService;

    @GetMapping
    String signin(@RequestParam String uuid, String socialUserId) {
        AbstractProfile profile = profileService.getByConfirmUuid(uuid);

        if (profile == null) {
            return "/expired_link";
        }
        
        profile.setConfirmUuid(UUID.randomUUID().toString());//Одну и ту же ссылку нельзя использовать дважды
        profile.setConfirmed(true);

        profileService.update(profile);
        
        if (socialUserId != null) {
            socialService.updateSocialConnection(socialUserId, profile.getEmail());
        }
        
        securityService.auth(profile, true);

        return "redirect:/";
    }
}