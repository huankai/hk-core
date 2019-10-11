package com.hk.core.autoconfigure.authentication.apereo.cas;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.cas.authentication.CasAuthenticationProvider;

import com.hk.commons.util.IDGenerator;

import lombok.Data;

/**
 * @author huangkai
 * @date 2019-01-25 17:28
 */
@Data
@ConfigurationProperties(prefix = "hk.authentication.cas")
public class ApereoCasProperties {
	
	/**
	 * @see CasAuthenticationProvider#setKey(String)
	 */
	private String key = IDGenerator.STRING_UUID.generate();

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
    private String ticketValidatorUrl;

    /**
     */
    private String casServerUrlPrefix;
    
    /**
     * 
     * @see org.springframework.security.web.authentication.logout.LogoutFilter#logoutSuccessHandler
     */
    private String casServerLogoutUrl;


}
