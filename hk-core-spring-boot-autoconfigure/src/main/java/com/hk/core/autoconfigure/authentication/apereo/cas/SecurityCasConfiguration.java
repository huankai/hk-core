package com.hk.core.autoconfigure.authentication.apereo.cas;

import com.hk.authentication.security.cas.userdetails.CustomGrantedAuthorityFromAssertionAttributesUserDetailsService;
import com.hk.core.autoconfigure.authentication.security.AuthenticationProperties;
import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.session.SingleSignOutHttpSessionListener;
import org.jasig.cas.client.validation.Cas30ServiceTicketValidator;
import org.jasig.cas.client.validation.TicketValidator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.event.EventListener;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.cas.authentication.CasAuthenticationProvider;
import org.springframework.security.cas.web.CasAuthenticationEntryPoint;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

import javax.servlet.http.HttpSessionEvent;
import java.util.Collections;

/**
 * @author huangkai
 * @date 2019-01-25 17:42
 */
@Configuration
@ConditionalOnClass(value = {CasAuthenticationProvider.class})
@EnableConfigurationProperties({ApereoCasProperties.class, AuthenticationProperties.class})
public class SecurityCasConfiguration {

    private final ApereoCasProperties apereoCasProperties;

    private final AuthenticationProperties authenticationProperties;

    public SecurityCasConfiguration(ApereoCasProperties apereoCasProperties, AuthenticationProperties authenticationProperties) {
        this.apereoCasProperties = apereoCasProperties;
        this.authenticationProperties = authenticationProperties;
    }

    @Bean
    public ServiceProperties serviceProperties() {
        ServiceProperties serviceProperties = new ServiceProperties();
        serviceProperties.setSendRenew(apereoCasProperties.isSendRenew());
        serviceProperties.setService(apereoCasProperties.getService());
        return serviceProperties;
    }

    @Bean
    @Primary
    public AuthenticationEntryPoint authenticationEntryPoint() {
        CasAuthenticationEntryPoint entryPoint = new CasAuthenticationEntryPoint();
        entryPoint.setLoginUrl(apereoCasProperties.getCasLoginUrl());
        entryPoint.setServiceProperties(serviceProperties());
        return entryPoint;
    }

    @Bean
    public TicketValidator ticketValidator() {
        return new Cas30ServiceTicketValidator(apereoCasProperties.getTicketValidatorUrl());
    }

    /**
     * CAS 认证
     */
    @Bean
    public CasAuthenticationProvider casAuthenticationProvider() {
        CasAuthenticationProvider provider = new CasAuthenticationProvider();
        provider.setServiceProperties(serviceProperties());
        provider.setTicketValidator(ticketValidator());
        provider.setAuthenticationUserDetailsService(new CustomGrantedAuthorityFromAssertionAttributesUserDetailsService(Collections.emptySet(), Collections.emptySet()));
        provider.setKey(apereoCasProperties.getKey());
        return provider;
    }


    @Bean
    public SecurityContextLogoutHandler securityContextLogoutHandler() {
        return new SecurityContextLogoutHandler();
    }

    @Bean
    public LogoutFilter logoutFilter() {
        LogoutFilter logoutFilter = new LogoutFilter(apereoCasProperties.getCasServerLogoutUrl(),
                securityContextLogoutHandler());
        logoutFilter.setFilterProcessesUrl(authenticationProperties.getLogin().getLogoutUrl());
        return logoutFilter;
    }

    @Bean
    public SingleSignOutFilter singleSignOutFilter() {
        //单点退出
        SingleSignOutFilter singleSignOutFilter = new SingleSignOutFilter();
        singleSignOutFilter.setCasServerUrlPrefix(apereoCasProperties.getCasServerUrlPrefix());
        singleSignOutFilter.setIgnoreInitConfiguration(true);
        return singleSignOutFilter;
    }

    //设置退出监听
    @EventListener
    public SingleSignOutHttpSessionListener singleSignOutHttpSessionListener(HttpSessionEvent event) {
        return new SingleSignOutHttpSessionListener();
    }


}
