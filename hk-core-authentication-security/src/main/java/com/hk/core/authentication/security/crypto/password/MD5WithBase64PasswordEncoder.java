package com.hk.core.authentication.security.crypto.password;

import com.hk.commons.util.Algorithm;
import com.hk.commons.util.Base64Utils;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.MessageDigest;

/**
 * @author kevin
 * @date 2019-12-30 11:09
 */
public class MD5WithBase64PasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(CharSequence rawPassword) {
        try {
            MessageDigest md = MessageDigest.getInstance(Algorithm.MD5.getName());
            byte[] b = md.digest(rawPassword.toString().getBytes());
            return Base64Utils.encodeToString(b);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return encode(rawPassword).equals(encodedPassword);
    }
}
