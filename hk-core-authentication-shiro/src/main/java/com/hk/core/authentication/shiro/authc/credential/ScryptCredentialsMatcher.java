package com.hk.core.authentication.shiro.authc.credential;

import com.hk.commons.util.ObjectUtils;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.mindrot.jbcrypt.BCrypt;

/**
 * 使用  Scrypt 加密
 *
 * @author huangkai
 * @date 2018-12-25 13:53
 */
public class ScryptCredentialsMatcher implements CredentialsMatcher {

    private static ScryptCredentialsMatcher INSTANCE = new ScryptCredentialsMatcher();

    private ScryptCredentialsMatcher() {

    }

    public static ScryptCredentialsMatcher getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        return BCrypt.checkpw(ObjectUtils.toString(token.getCredentials()), String.valueOf(info.getCredentials()));
    }
}
