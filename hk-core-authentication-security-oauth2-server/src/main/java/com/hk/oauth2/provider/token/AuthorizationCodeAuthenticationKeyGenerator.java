package com.hk.oauth2.provider.token;

import com.hk.core.authentication.oauth2.AuthenticationType;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import java.io.Serializable;
import java.util.Map;

public class AuthorizationCodeAuthenticationKeyGenerator extends AbstractAuthenticationKeyGenerator {

    public AuthorizationCodeAuthenticationKeyGenerator() {
        super(AuthenticationType.authorization_code.name());
    }

    @Override
    protected Map<String, Serializable> extractKeyMap(OAuth2Authentication authentication) {
        return authentication.getOAuth2Request().getExtensions();
    }
}
