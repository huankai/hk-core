package com.hk.core.authentication.shiro.realm;

import com.hk.core.authentication.shiro.UserDetailsService;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;

/**
 * <pre>
 * {MD5}
 * {SHA-1}
 * {SHA-256}
 *
 * </pre>
 *
 * @author huangkai
 * @date 2018-12-25 13:52
 */
public class HashedAuthorizingRealm extends AbstractAuthorizingRealm {

    /**
     * @param hashAlgorithmName  such as : MD5 / SHA-1 / SHA-256
     * @param userDetailsService {@link UserDetailsService}
     */
    public HashedAuthorizingRealm(String hashAlgorithmName, UserDetailsService userDetailsService) {
        super(userDetailsService);
        setCredentialsMatcher(new HashedCredentialsMatcher(hashAlgorithmName));
    }
}
