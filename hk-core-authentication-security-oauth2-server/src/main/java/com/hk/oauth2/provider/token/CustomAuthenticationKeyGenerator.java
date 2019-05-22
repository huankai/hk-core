package com.hk.oauth2.provider.token;

import com.hk.commons.util.CollectionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AuthenticationKeyGenerator;

import java.io.Serializable;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author kevin
 * @date 2019-5-10 20:44
 */
@RequiredArgsConstructor
public class CustomAuthenticationKeyGenerator implements AuthenticationKeyGenerator {

    private static final String USERNAME = "username";

    public String extractKey(OAuth2Authentication authentication) {
        Map<String, Object> values = new LinkedHashMap<>();
        values.put(USERNAME, authentication.getName());
        Map<String, Serializable> extensions = authentication.getOAuth2Request().getExtensions();
        if (CollectionUtils.isNotEmpty(extensions)) {
            values.putAll(extensions);
        }
        return generateKey(values);
    }

    protected String generateKey(Map<String, Object> values) {
        Map<String, Object> keyMap = CollectionUtils.sortMapByKey(values);
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
            byte[] bytes = digest.digest(keyMap.toString().getBytes(StandardCharsets.UTF_8));
            return String.format("%032x", new BigInteger(1, bytes));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("MD5 algorithm not available.  Fatal (should be in the JDK).", e);
        }
    }
}
