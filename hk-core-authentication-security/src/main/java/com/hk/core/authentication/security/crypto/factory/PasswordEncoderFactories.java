package com.hk.core.authentication.security.crypto.factory;

import com.hk.commons.util.Algorithm;
import com.hk.core.authentication.security.crypto.password.MD5WithBase64PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;

import java.util.HashMap;
import java.util.Map;

/**
 * 重写 Spring 的 {@link org.springframework.security.crypto.factory.PasswordEncoderFactories}
 *
 * @author kevin
 * @date 2019-12-30 11:05
 */
public class PasswordEncoderFactories {

    public static PasswordEncoder createDelegatingPasswordEncoder() {
        String encodingId = "bcrypt";
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put(encodingId, new BCryptPasswordEncoder());
        encoders.put("MD5-B64", new MD5WithBase64PasswordEncoder());
        encoders.put("noop", org.springframework.security.crypto.password.NoOpPasswordEncoder.getInstance());
        encoders.put("pbkdf2", new Pbkdf2PasswordEncoder());
        encoders.put("scrypt", new SCryptPasswordEncoder());
        encoders.put("MD5", new org.springframework.security.crypto.password.MessageDigestPasswordEncoder(Algorithm.MD5.getName()));
        encoders.put("SHA-1", new org.springframework.security.crypto.password.MessageDigestPasswordEncoder(Algorithm.SHA_1.getName()));
        encoders.put("SHA-256", new org.springframework.security.crypto.password.MessageDigestPasswordEncoder(Algorithm.SHA_256.getName()));
        return new DelegatingPasswordEncoder(encodingId, encoders);
    }

    private PasswordEncoderFactories() {
    }
}
