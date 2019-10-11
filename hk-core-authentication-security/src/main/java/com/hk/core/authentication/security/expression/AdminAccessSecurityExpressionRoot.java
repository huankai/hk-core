package com.hk.core.authentication.security.expression;

import java.io.Serializable;
import java.util.Set;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.access.expression.SecurityExpressionOperations;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.core.Authentication;

import com.hk.commons.util.ArrayUtils;
import com.hk.commons.util.CollectionUtils;
import com.hk.commons.util.StringUtils;
import com.hk.core.authentication.api.UserPrincipal;
import com.hk.core.authentication.security.SecurityUserPrincipal;

import lombok.Setter;

/**
 * <p>
 * 重写角色与权限<br/>
 * 如果当前登陆的用户是 administrator或 是保护的用户，拥有所有的权限
 * </p>
 *
 * @author kevin
 * @date 2018-09-14 10:59
 * @see org.springframework.security.access.expression.SecurityExpressionRoot
 */
public class AdminAccessSecurityExpressionRoot implements SecurityExpressionOperations {


    protected final Authentication authentication;

    @Setter
    private AuthenticationTrustResolver trustResolver;

    @Setter
    private RoleHierarchy roleHierarchy;
//
//    private Set<String> roles;

    @Setter
    private String defaultRolePrefix = SecurityUserPrincipal.ROLE_PREFIX;

    /**
     * Allows "permitAll" expression
     */
    public final boolean permitAll = true;
//
    /**
     * Allows "denyAll" expression
     */
    public final boolean denyAll = false;

    @Setter
    private PermissionEvaluator permissionEvaluator;

    public final String read = "read";

    public final String write = "write";

    public final String create = "create";

    public final String delete = "delete";

    public final String admin = "administration";

    /**
     * Creates a new instance
     *
     * @param authentication the {@link Authentication} to use. Cannot be null.
     */
    public AdminAccessSecurityExpressionRoot(Authentication authentication) {
        if (authentication == null) {
            throw new IllegalArgumentException("Authentication object cannot be null");
        }
        this.authentication = authentication;
    }

    @Override
    public boolean hasAuthority(String authority) {
        return hasAnyAuthority(authority);
    }

    /**
     * @param authorities authorities
     * @return
     * @see org.springframework.security.access.expression.SecurityExpressionRoot#hasAnyAuthority(String...)
     */
    @Override
    public boolean hasAnyAuthority(String... authorities) {
        if (isAnonymous()) {// 为什么要这么判断，因为调用此方法时，用户可能是未登陆的用户
            return false;
        }
        UserPrincipal userPrincipal = UserPrincipal.class.cast(authentication.getPrincipal());
        return userPrincipal.isAdministrator()
                || CollectionUtils.containsAny(userPrincipal.getPermissions(), authorities)
                || hasAnyAuthorityName(null, authorities);
    }

    @Override
    public final boolean hasRole(String role) {
        return hasAnyRole(role);
    }

    /**
     * @param roles roles
     * @return boolean
     * @see org.springframework.security.access.expression.SecurityExpressionRoot#hasAnyRole(String...)
     */
    @Override
    public final boolean hasAnyRole(String... roles) {
        if (ArrayUtils.isEmpty(roles) || isAnonymous()) {// 为什么要这么判断，因为调用此方法时，用户可能是未登陆的用户
            return false;
        }
        UserPrincipal userPrincipal = UserPrincipal.class.cast(authentication.getPrincipal());
        for (String role : roles) {
            if (userPrincipal.hasRole(role)) {
                return true;
            }
        }
        return hasAnyAuthorityName(defaultRolePrefix, roles);
    }

    private boolean hasAnyAuthorityName(String prefix, String... roles) {
        Set<String> permissionSet = UserPrincipal.class.cast(authentication.getPrincipal()).getPermissions();
        for (String role : roles) {
            String defaultedRole = getRoleWithDefaultPrefix(prefix, role);
            if (CollectionUtils.contains(permissionSet, defaultedRole)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public final Authentication getAuthentication() {
        return authentication;
    }

    @Override
    public final boolean permitAll() {
        return true;
    }

    @Override
    public final boolean denyAll() {
        return false;
    }

    @Override
    public final boolean isAnonymous() {
        return trustResolver.isAnonymous(authentication);
    }

    @Override
    public final boolean isAuthenticated() {
        return !isAnonymous();
    }

    @Override
    public final boolean isRememberMe() {
        return trustResolver.isRememberMe(authentication);
    }

    @Override
    public final boolean isFullyAuthenticated() {
        return !trustResolver.isAnonymous(authentication)
                && !trustResolver.isRememberMe(authentication);
    }

    /**
     * Convenience method to access {@link Authentication#getPrincipal()} from
     * {@link #getAuthentication()}
     *
     * @return
     */
    public Object getPrincipal() {
        return authentication.getPrincipal();
    }

    @Override
    public boolean hasPermission(Object target, Object permission) {
        return permissionEvaluator.hasPermission(authentication, target, permission);
    }

    @Override
    public boolean hasPermission(Object targetId, String targetType, Object permission) {
        return permissionEvaluator.hasPermission(authentication, (Serializable) targetId, targetType, permission);
    }

    /**
     * Prefixes role with defaultRolePrefix if defaultRolePrefix is non-null and if role
     * does not already start with defaultRolePrefix.
     *
     * @param defaultRolePrefix
     * @param role
     * @return
     */
    private static String getRoleWithDefaultPrefix(String defaultRolePrefix, String role) {
        if (role == null) {
            return null;
        }
        if (StringUtils.isEmpty(defaultRolePrefix)) {
            return role;
        }
        if (role.startsWith(defaultRolePrefix)) {
            return role;
        }
        return defaultRolePrefix + role;
    }
}
