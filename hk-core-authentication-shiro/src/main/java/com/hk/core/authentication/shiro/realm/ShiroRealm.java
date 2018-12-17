package com.hk.core.authentication.shiro.realm;

import com.hk.commons.util.CollectionUtils;
import com.hk.core.authentication.api.UserPrincipal;
import com.hk.core.authentication.shiro.UserDetailsService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;


/**
 * Shiro Realm
 *
 * @author huangkai
 * @date 2018年12月17日 下午2:31:56
 * @since V2.1.0
 */
public class ShiroRealm extends AuthorizingRealm {

    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * 用户权限
     *
     * @param principalCollection principalCollection
     * @return {@link AuthorizationInfo}
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        UserPrincipal userPrincipal = principalCollection.oneByType(UserPrincipal.class);
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        Set<String> roleSet = userPrincipal.getRoleSet();
        if (CollectionUtils.isNotEmpty(roleSet)) {
            info.setRoles(roleSet);
        }
        Set<String> permissionSet = userPrincipal.getPermissionSet();
        if (CollectionUtils.isNotEmpty(permissionSet)) {
            info.setStringPermissions(permissionSet);
        }
        return info;
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
        UserPrincipal userPrincipal = userDetailsService.loadUserByLoginUsername(username);
        if (null == userPrincipal) {
            throw new UnknownAccountException("账号不存在:" + username);
        }
        PrincipalCollection principalCollection = new SimplePrincipalCollection(userPrincipal, getName());
        return new SimpleAuthenticationInfo(principalCollection, userPrincipal.hashCode());
    }
}
