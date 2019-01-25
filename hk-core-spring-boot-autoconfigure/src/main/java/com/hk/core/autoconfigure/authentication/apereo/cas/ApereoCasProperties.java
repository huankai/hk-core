package com.hk.core.autoconfigure.authentication.apereo.cas;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author huangkai
 * @date 2019-01-25 17:28
 */
@Data
@ConfigurationProperties(prefix = "hk.authentication.cas")
public class ApereoCasProperties {

    /**
     * @see org.springframework.security.cas.ServiceProperties#service
     */
    private String service;

    /**
     * @see org.springframework.security.cas.ServiceProperties#sendRenew
     */
    private boolean sendRenew;

    /**
     * @see org.springframework.security.cas.web.CasAuthenticationEntryPoint#loginUrl
     */
    private String casLoginUrl;

    /**
     * @see org.jasig.cas.client.validation.Cas30ServiceTicketValidator#casServerUrlPrefix
     */
    private String ticketValidatorUrl = getCasServerUrlPrefix();

    /**
     * @see org.springframework.security.web.authentication.logout.LogoutFilter#logoutSuccessHandler
     */
    private String casServerUrlPrefix;


}
