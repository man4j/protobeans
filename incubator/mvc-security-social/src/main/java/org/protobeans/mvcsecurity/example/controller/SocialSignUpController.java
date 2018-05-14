package org.protobeans.mvcsecurity.example.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.protobeans.mvc.controller.advice.RequestContextHolder;
import org.protobeans.mvcsecurity.example.service.EmailService;
import org.protobeans.mvcsecurity.example.service.InMemoryProfileService;
import org.protobeans.mvcsecurity.example.service.MessageGenerator;
import org.protobeans.security.annotation.Anonymous;
import org.protobeans.security.model.AbstractProfile;
import org.protobeans.security.service.SecurityService;
import org.protobeans.social.model.SetEmailForm;
import org.protobeans.social.service.ProtobeansSocialUserDetailsService;
import org.protobeans.social.service.SocialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/socialSignUp")
@Anonymous("/socialSignUp")
public class SocialSignUpController {
    @Autowired
    private HttpSession session;
    
    @Autowired
    private InMemoryProfileService profileService;
    
    @Autowired
    private ConnectionRepository repository;
    
    @Autowired
    private SocialService socialService;
    
    @Autowired
    private SecurityService securityService;
    
    @Autowired
    private EmailService emailService;
    
    @Autowired
    private MessageGenerator messageGenerator;
    
    @Autowired
    private RequestContextHolder requestContextHolder;
    
    @RequestMapping(method = RequestMethod.GET)
    public String get(@ModelAttribute("form") SetEmailForm form, Model model) {
        String socialUserId = (String) session.getAttribute(ProtobeansSocialUserDetailsService.NOT_EXISTING_USER_ID_ATTRIBUTE_NAME);
        Connection<?> connection = getSocialConnection(socialUserId);
        
        String socialEmail = connection.fetchUserProfile().getEmail();
        
        if (socialEmail != null) {
            AbstractProfile profile = profileService.getByEmail(socialEmail);
            
            if (profile != null) {
                return linkAndAuth(profile, socialUserId);
            }
        }
        
        prepareModel(form, model, connection);

        return "/social_signup";
    }
    
    @RequestMapping(method = RequestMethod.POST)
    public String post(@ModelAttribute("form") @Valid SetEmailForm form, BindingResult result, Model model) {
        String socialUserId = (String) session.getAttribute(ProtobeansSocialUserDetailsService.NOT_EXISTING_USER_ID_ATTRIBUTE_NAME);
        Connection<?> connection = getSocialConnection(socialUserId);
        
        if (result.hasErrors()) {
            prepareModel(form, model, connection);
            
            return "/social_signup";
        }
        
        AbstractProfile profile = profileService.getByEmail(form.getEmail());//связываем с профайлом заданным юзером вручную
        
        boolean isConfirmed = form.getEmail().equals(connection.fetchUserProfile().getEmail());
        
        if (profile == null) {
            profile = profileService.create(form.getEmail(), securityService.generatePassword(), form.getEmail(), isConfirmed);
        }
        
        return isConfirmed ? linkAndAuth(profile, socialUserId) : requestEmailConfirmation(profile, socialUserId);
    }
    
    private String linkAndAuth(AbstractProfile profile, String socialUserId) {
        session.setAttribute(ProtobeansSocialUserDetailsService.NOT_EXISTING_USER_ID_ATTRIBUTE_NAME, null);
        
        socialService.updateSocialConnection(socialUserId, profile.getEmail());
        securityService.auth(profile, true);
        
        return "redirect:/";
    }
    
    private String requestEmailConfirmation(AbstractProfile profile, String socialUserId) {
        session.setAttribute(ProtobeansSocialUserDetailsService.NOT_EXISTING_USER_ID_ATTRIBUTE_NAME, null);
        
        emailService.sendMessage(profile.getEmail(), messageGenerator.generateEmailSignInMessage(profile.getPassword(), profile.getConfirmUuid(), socialUserId, requestContextHolder.getRequestContext()));

        return "/check_email";
    } 
    
    private void prepareModel(SetEmailForm form, Model model, Connection<?> connection) {
        form.setEmail(connection.fetchUserProfile().getEmail());        
        model.addAttribute("name", connection.getDisplayName());
        model.addAttribute("img", connection.getImageUrl());
    }
    
    private Connection<?> getSocialConnection(String socialUserId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        try {
            //create fake authentication
            SecurityContextHolder.getContext().setAuthentication(new PreAuthenticatedAuthenticationToken(socialUserId, null));
            
            //get connection for unauthenticated user
            return repository.getConnection(socialService.getConnectionKeyByUserId(socialUserId));
        } finally {
            //restore prev authentication
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
    }
}
