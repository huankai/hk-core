package com.hk.oauth2.provider.token;

import com.hk.commons.util.Algorithm;
import com.hk.commons.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AuthenticationKeyGenerator;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@RequiredArgsConstructor
public abstract class AbstractAuthenticationKeyGenerator implements AuthenticationKeyGenerator {

    private static final String USERNAME = "username";

    private final String grantType;

    @Override
    public String extractKey(OAuth2Authentication authentication) {
        String grantType = authentication.getOAuth2Request().getGrantType();
        if (StringUtils.equals(this.grantType, grantType)) {
            Map<String, Object> values = new HashMap<>();
            values.put(USERNAME, authentication.getName());
            values.putAll(extractKeyMap(authentication));
            return generateKey(values);
        }
        return null;
    }

    private String generateKey(Map<String, Object> values) {
        Map<String, Object> finalMap = new LinkedHashMap<>();
        values.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEachOrdered(e -> finalMap.put(e.getKey(), e.getValue()));
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance(Algorithm.MD5.getName());
            byte[] bytes = digest.digest(finalMap.toString().getBytes(StandardCharsets.UTF_8));
            return String.format("%032x", new BigInteger(1, bytes));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("MD5 algorithm not available.  Fatal (should be in the JDK).", e);
        }
    }

    protected abstract Map<String, ?> extractKeyMap(OAuth2Authentication authentication);

}
