package com.hk.oauth2.provider.token;

import com.hk.commons.util.ArrayUtils;
import com.hk.commons.util.StringUtils;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AuthenticationKeyGenerator;

import java.util.List;

public final class CompositeAuthenticationKeyGenerator implements AuthenticationKeyGenerator {

    private final List<AuthenticationKeyGenerator> authenticationKeyGenerators;

    public CompositeAuthenticationKeyGenerator() {
        this.authenticationKeyGenerators = ArrayUtils.asArrayList(new AuthorizationCodeAuthenticationKeyGenerator(),
                new PasswordAuthenticationKeyGenerator(), new RefreshAuthenticationKeyGenerator());
    }

    public CompositeAuthenticationKeyGenerator(List<AuthenticationKeyGenerator> authenticationKeyGenerators) {
        this.authenticationKeyGenerators = authenticationKeyGenerators;
    }

    @Override
    public String extractKey(OAuth2Authentication authentication) {
        for (var keyGenerator : authenticationKeyGenerators) {
            var key = keyGenerator.extractKey(authentication);
            if (StringUtils.isNotEmpty(key)) {
                return key;
            }
        }
        throw new AuthenticationServiceException("不能获取 Key");
    }
}
