package com.hk.core.autoconfigure.authentication.security;

import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

/**
 * 记住我配置
 *
 * @author: kevin
 * @date 2018-07-27 16:09
 */
public class RememberMeAuthenticationSecurityConfiguration extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private AuthenticationProperties.RememberMeProperties rememberMe;

    private final AuthenticationSuccessHandler authenticationSuccessHandler;

    private final UserDetailsService userDetailsService;

    private PersistentTokenRepository persistentTokenRepository;

    public RememberMeAuthenticationSecurityConfiguration(AuthenticationProperties.RememberMeProperties rememberMe,
                                                         AuthenticationSuccessHandler authenticationSuccessHandler,
                                                         UserDetailsService userDetailsService, PersistentTokenRepository persistentTokenRepository) {
        this.rememberMe = rememberMe;
        this.authenticationSuccessHandler = authenticationSuccessHandler;
        this.userDetailsService = userDetailsService;
        this.persistentTokenRepository = persistentTokenRepository;
    }


    @Override
    public void configure(HttpSecurity http) throws Exception {
        if (rememberMe.isEnabledRememberMe()) {
            http.rememberMe()
                    .tokenRepository(persistentTokenRepository)
                    .useSecureCookie(rememberMe.isUseSecureCookie())
                    .tokenValiditySeconds(rememberMe.getRememberMeSeconds())
                    .rememberMeParameter(rememberMe.getRememberMeParameter())
                    .authenticationSuccessHandler(authenticationSuccessHandler)
                    .userDetailsService(userDetailsService);

        }
    }
}
