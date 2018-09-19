package com.hk.core.authentication.security.expression;

import com.hk.core.authentication.security.SecurityUserPrincipal;
import org.springframework.security.access.expression.AbstractSecurityExpressionHandler;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.access.expression.SecurityExpressionOperations;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;
import org.springframework.util.Assert;


/**
 * 拥有 admin 角色 或 admin 权限的用户，或者是保护用户可以直接访问
 *
 * @author: sjq-278
 * @date: 2018-09-14 10:58
 * @see DefaultWebSecurityExpressionHandler
 */
public class AdminAccessWebSecurityExpressionHandler extends AbstractSecurityExpressionHandler<FilterInvocation> implements SecurityExpressionHandler<FilterInvocation> {

    private AuthenticationTrustResolver trustResolver = new AuthenticationTrustResolverImpl();

    private String defaultRolePrefix = SecurityUserPrincipal.ROLE_PREFIX;

    @Override
    protected SecurityExpressionOperations createSecurityExpressionRoot(Authentication authentication, FilterInvocation fi) {
        AdminAccessSecurityExpressionRoot expressionRoot = new AdminAccessSecurityExpressionRoot(authentication);
        expressionRoot.setPermissionEvaluator(getPermissionEvaluator());
        expressionRoot.setTrustResolver(trustResolver);
        expressionRoot.setRoleHierarchy(getRoleHierarchy());
        expressionRoot.setDefaultRolePrefix(defaultRolePrefix);
        return expressionRoot;
    }

    /**
     * Sets the {@link AuthenticationTrustResolver} to be used. The default is
     * {@link AuthenticationTrustResolverImpl}.
     *
     * @param trustResolver the {@link AuthenticationTrustResolver} to use. Cannot be
     *                      null.
     */
    public void setTrustResolver(AuthenticationTrustResolver trustResolver) {
        Assert.notNull(trustResolver, "trustResolver cannot be null");
        this.trustResolver = trustResolver;
    }

    /**
     * <p>
     * Sets the default prefix to be added to {@link org.springframework.security.access.expression.SecurityExpressionRoot#hasAnyRole(String...)} or
     * {@link org.springframework.security.access.expression.SecurityExpressionRoot#hasRole(String)}. For example, if hasRole("ADMIN") or hasRole("ROLE_ADMIN")
     * is passed in, then the role ROLE_ADMIN will be used when the defaultRolePrefix is
     * "ROLE_" (default).
     * </p>
     *
     * <p>
     * If null or empty, then no default role prefix is used.
     * </p>
     *
     * @param defaultRolePrefix the default prefix to add to roles. Default "ROLE_".
     */
    public void setDefaultRolePrefix(String defaultRolePrefix) {
        this.defaultRolePrefix = defaultRolePrefix;
    }
}
