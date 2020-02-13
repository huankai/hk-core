package com.hk.core.authentication.oauth2;

import org.springframework.security.crypto.factory.PasswordEncoderFactories;

/**
 * @author kevin
 * @date 2020-02-13 18:07
 */
public class Test {

    public static void main(String[] args) {
        System.out.println(PasswordEncoderFactories.createDelegatingPasswordEncoder().encode("123456"));
    }
}
