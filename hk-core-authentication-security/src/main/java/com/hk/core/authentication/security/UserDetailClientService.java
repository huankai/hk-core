package com.hk.core.authentication.security;

import com.hk.commons.util.AssertUtils;
import com.hk.core.authentication.api.ClientAppInfo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * <pre>
 *
 * 获取用户信息与 clientId 应用信息接口，如果不是集成单点登陆或 Oauth2 登陆的客户端认证，请直接使用 {@link UserDetailsService}
 * </pre>
 *
 * @author kevin
 */
public interface UserDetailClientService extends UserDetailsService {

    @Override
    default UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AssertUtils.notEmpty(username, "用户名不能为空");
        return loadUserByLoginUsername(username);
    }

    /**
     * 获取用户信息
     *
     * @param username username
     * @return {@link SecurityUserPrincipal}
     */
    SecurityUserPrincipal loadUserByLoginUsername(String username);

    /**
     * 获取客户端信息
     *
     * @param clientId clientId
     * @return ClientAppInfo
     */
    ClientAppInfo getClientInfoById(Long clientId);


}
