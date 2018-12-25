package com.hk.core.authentication.shiro.realm;

import com.hk.commons.util.StringUtils;
import com.hk.core.authentication.api.SecurityContextUtils;
import com.hk.core.authentication.api.UserPrincipal;
import com.hk.core.authentication.shiro.ShiroUserPrincipal;
import com.hk.core.authentication.shiro.UserDetailsService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;


/**
 * Shiro Realm
 *
 * @author huangkai
 * @date 2018年12月17日 下午2:31:56
 * @since V2.1.0
 */
public abstract class AbstractAuthorizingRealm extends AuthorizingRealm {

    private static final String SUFFIX = "}";

    private UserDetailsService userDetailsService;

    AbstractAuthorizingRealm(UserDetailsService userDetailsService) {
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
        UserPrincipal principal = principalCollection.oneByType(UserPrincipal.class);
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo(principal.getPermissionSet());
        authorizationInfo.setStringPermissions(principal.getPermissionSet());
        return authorizationInfo;
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
        Object username = authenticationToken.getPrincipal();
        if (null == username) {
            throw new AuthenticationException("账号不能为空");
        }
        ShiroUserPrincipal userPrincipal = userDetailsService.loadUserByLoginUsername(String.valueOf(username))
                .orElseThrow(() -> new UnknownAccountException("账号不存在:" + username));
        return new SimpleAuthenticationInfo(userPrincipal.getUserPrincipal(),
                StringUtils.substringAfter(userPrincipal.getPassword(), SUFFIX), getName());
    }

    /**
     * 判断用户是否有权限
     *
     * @param permission 权限
     * @param info       用户信息
     * @return true or false
     */
    @Override
    protected boolean isPermitted(Permission permission, AuthorizationInfo info) {
        return SecurityContextUtils.getPrincipal().isAdministrator() || super.isPermitted(permission, info);
    }


}
