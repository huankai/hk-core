package com.hk.core.authentication.security;

import com.hk.commons.util.AssertUtils;
import com.hk.core.authentication.api.ClientAppInfo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @author kevin
 */
public interface UserDetailClientService extends UserDetailsService {

    @Override
    default UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AssertUtils.notBlank(username, "用户名不能为空");
        return loadUserByLoginUsername(username);
    }

    SecurityUserPrincipal loadUserByLoginUsername(String username);

    /**
     * 获取客户端信息
     *
     * @param clientId clientId
     * @return ClientAppInfo
     */
    ClientAppInfo getClientInfoById(String clientId);


}
