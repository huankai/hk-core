package com.hk.oauth2.provider.token;

import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AuthenticationKeyGenerator;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

/**
 * 
 * 使用 details 签名 {@link WebAuthenticationDetails}
 * @author huangkai
 *
 */
public class SessionAuthenticationKeyGenerator implements AuthenticationKeyGenerator {

	@Override
	public String extractKey(OAuth2Authentication authentication) {
		WebAuthenticationDetails details = (WebAuthenticationDetails) authentication.getUserAuthentication()
				.getDetails();
		return details.getSessionId();
	}

}
