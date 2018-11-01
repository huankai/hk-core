package com.hk.core.authentication.security.expression;

import com.hk.commons.util.CollectionUtils;
import com.hk.core.authentication.api.UserPrincipal;
import com.hk.core.authentication.security.SecurityUserPrincipal;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.access.expression.SecurityExpressionOperations;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>
 * 重写角色与权限<br/>
 * 如果当前登陆的用户是 administrator或 是保护的用户，拥有所有的权限
 * </p>
 *
 * @author: kevin
 * @date: 2018-09-14 10:59
 * @see org.springframework.security.access.expression.SecurityExpressionRoot
 */
public class AdminAccessSecurityExpressionRoot implements SecurityExpressionOperations {


    protected final Authentication authentication;

    private AuthenticationTrustResolver trustResolver;

    private RoleHierarchy roleHierarchy;

    private Set<String> roles;

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
        Object principal = authentication.getPrincipal();
        UserPrincipal userPrincipal = (UserPrincipal) principal;
        return userPrincipal.isAdministrator()
                || userPrincipal.isProtectUser()
                || CollectionUtils.containsAny(userPrincipal.getPermissionSet(), authorities)
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
        if (isAnonymous()) {// 为什么要这么判断，因为调用此方法时，用户可能是未登陆的用户
            return false;
        }
        Object principal = authentication.getPrincipal();
        UserPrincipal userPrincipal = (UserPrincipal) principal;
        return userPrincipal.isAdministrator()
                || userPrincipal.isProtectUser()
                || CollectionUtils.containsAny(userPrincipal.getRoleSet(), roles)
                || hasAnyAuthorityName(defaultRolePrefix, roles);
    }

    private boolean hasAnyAuthorityName(String prefix, String... roles) {
        Set<String> roleSet = getAuthoritySet();
        for (String role : roles) {
            String defaultedRole = getRoleWithDefaultPrefix(prefix, role);
            if (CollectionUtils.contains(roleSet, defaultedRole)) {
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

    public void setTrustResolver(AuthenticationTrustResolver trustResolver) {
        this.trustResolver = trustResolver;
    }

    public void setRoleHierarchy(RoleHierarchy roleHierarchy) {
        this.roleHierarchy = roleHierarchy;
    }

    /**
     * <p>
     * Sets the default prefix to be added to {@link #hasAnyRole(String...)} or
     * {@link #hasRole(String)}. For example, if hasRole("ADMIN") or hasRole("ROLE_ADMIN")
     * is passed in, then the role ROLE_ADMIN will be used when the defaultRolePrefix is
     * "ROLE_" (default).
     * </p>
     * <p>
     * <p>
     * If null or empty, then no default role prefix is used.
     * </p>
     *
     * @param defaultRolePrefix the default prefix to add to roles. Default "ROLE_".
     */
    public void setDefaultRolePrefix(String defaultRolePrefix) {
        this.defaultRolePrefix = defaultRolePrefix;
    }

    private Set<String> getAuthoritySet() {
        if (roles == null) {
            roles = new HashSet<>();
            Collection<? extends GrantedAuthority> userAuthorities = authentication.getAuthorities();
            if (roleHierarchy != null) {
                userAuthorities = roleHierarchy
                        .getReachableGrantedAuthorities(userAuthorities);
            }
            roles = AuthorityUtils.authorityListToSet(userAuthorities);
        }
        return roles;
    }

    @Override
    public boolean hasPermission(Object target, Object permission) {
        return permissionEvaluator.hasPermission(authentication, target, permission);
    }

    @Override
    public boolean hasPermission(Object targetId, String targetType, Object permission) {
        return permissionEvaluator.hasPermission(authentication, (Serializable) targetId, targetType, permission);
    }

    public void setPermissionEvaluator(PermissionEvaluator permissionEvaluator) {
        this.permissionEvaluator = permissionEvaluator;
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
        if (defaultRolePrefix == null || defaultRolePrefix.length() == 0) {
            return role;
        }
        if (role.startsWith(defaultRolePrefix)) {
            return role;
        }
        return defaultRolePrefix + role;
    }
}
