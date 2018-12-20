package com.hk.core.authentication.shiro.realm;

import com.hk.core.authentication.shiro.UserDetailsService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;


/**
 * Shiro Realm
 *
 * @author huangkai
 * @date 2018年12月17日 下午2:31:56
 * @since V2.1.0
 */
public class CustomAuthorizingRealm extends AuthorizingRealm {

    private UserDetailsService userDetailsService;

    public CustomAuthorizingRealm(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    /**
     * 用户权限
     *
     * @param principalCollection principalCollection
     * @return {@link AuthorizationInfo}
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return  principalCollection.oneByType(Account.class);
    }

    /**
     * 用户认证
     *
     * @param authenticationToken authenticationToken
     * @return {@link AuthenticationInfo}
     * @throws AuthenticationException AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        Object principal = authenticationToken.getPrincipal();
        if (null == principal) {
            throw new AuthenticationException("账号不能为空");
        }
        String username = String.valueOf(principal);
        return userDetailsService.loadUserByLoginUsername(username)
                .orElseThrow(() -> new UnknownAccountException("账号不存在:" + username));
    }
}
