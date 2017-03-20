package org.protobeans.social.config;

import java.util.List;
import java.util.UUID;

import javax.sql.DataSource;

import org.protobeans.social.controller.CloseWindowController;
import org.protobeans.social.service.SocialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.UserIdSource;
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurer;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.security.AuthenticationNameUserIdSource;
import org.springframework.social.security.SpringSocialConfigurer;

@Configuration
@EnableSocial
@ComponentScan(basePackageClasses = {SocialService.class, CloseWindowController.class})
public class SocialWebConfig implements SocialConfigurer {
    @Autowired
    private DataSource dataSource;
    
    @Autowired
    private List<ConnectionFactory<?>> connectionFactories;
    
    @Override
    public void addConnectionFactories(ConnectionFactoryConfigurer conf, Environment env) {
        for (ConnectionFactory<?> cf : connectionFactories) {
            conf.addConnectionFactory(cf);
        }
    }

    @Override
    public UserIdSource getUserIdSource() {
        return new AuthenticationNameUserIdSource();
    }

    @Override
    public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator locator) {
        JdbcUsersConnectionRepository repository = new JdbcUsersConnectionRepository(dataSource, locator, Encryptors.noOpText());
        
        repository.setConnectionSignUp(c -> UUID.randomUUID().toString());
        
        return repository;
    }
    
    @Bean
    public SpringSocialConfigurer springSocialSecurityConfigurer() {
        return new SpringSocialConfigurer().signupUrl("/closeWindow")
                                           .postFailureUrl("/closeWindow")
                                           .postLoginUrl("/closeWindow")
                                           .alwaysUsePostLoginUrl(true);
    }
}
