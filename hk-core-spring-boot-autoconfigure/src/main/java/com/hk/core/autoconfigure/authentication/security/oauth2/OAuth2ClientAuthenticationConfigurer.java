package com.hk.core.autoconfigure.authentication.security.oauth2;

import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

/**
 * @author kevin
 * @date 2018-08-17 12:15
 * @see org.springframework.boot.autoconfigure.security.oauth2.client.SsoSecurityConfigurer.OAuth2ClientAuthenticationConfigurer
 */
@Deprecated
public class OAuth2ClientAuthenticationConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private OAuth2ClientAuthenticationProcessingFilter filter;

    public OAuth2ClientAuthenticationConfigurer(OAuth2ClientAuthenticationProcessingFilter filter) {
        this.filter = filter;
    }

    @Override
    public void configure(HttpSecurity builder) {
        OAuth2ClientAuthenticationProcessingFilter ssoFilter = this.filter;
        ssoFilter.setSessionAuthenticationStrategy(builder.getSharedObject(SessionAuthenticationStrategy.class));
        builder.addFilterBefore(ssoFilter, AbstractPreAuthenticatedProcessingFilter.class);
        postProcess(ssoFilter);
    }
}
