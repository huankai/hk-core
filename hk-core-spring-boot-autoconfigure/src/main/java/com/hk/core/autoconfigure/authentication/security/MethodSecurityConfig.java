package com.hk.core.autoconfigure.authentication.security;

import com.hk.commons.util.CollectionUtils;
import com.hk.core.authentication.api.UserPrincipal;
import com.hk.core.authentication.security.SpringSecurityContext;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.access.expression.SecurityExpressionOperations;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author: kevin
 * @date 2018-05-16 15:57
 */
@Configuration
@ConditionalOnClass(SpringSecurityContext.class)
@EnableGlobalMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
public class MethodSecurityConfig extends GlobalMethodSecurityConfiguration {

    public PermissionEvaluator permissionEvaluator() {
        return new PermissionEvaluator() {

            /**
             * 普通的 targetDomainObject 判断
             * @param authentication authentication
             * @param targetDomainObject targetDomainObject
             * @param permission permission
             * @return
             */
            @Override
            public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
                UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
                return principal.isAdministrator()
                        || principal.isProtectUser()
                        || principal.getPermissionSet().contains(String.valueOf(permission));
            }

            /**
             * ACL访问控制
             * @param authentication authentication
             * @param targetId targetId
             * @param targetType targetType
             * @param permission permission
             * @return
             */
            @Override
            public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
                return false;
            }
        };
    }

    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        return new DefaultMethodSecurityExpressionHandler() {
            @Override
            protected MethodSecurityExpressionOperations createSecurityExpressionRoot(Authentication authentication, MethodInvocation invocation) {
                ProtectedMethodSecurityExpressionRoot root = new ProtectedMethodSecurityExpressionRoot(authentication);
                root.setThis(invocation.getThis());
                root.setPermissionEvaluator(permissionEvaluator());
                root.setTrustResolver(getTrustResolver());
                root.setRoleHierarchy(getRoleHierarchy());
                root.setDefaultRolePrefix(getDefaultRolePrefix());
                return root;
            }
        };
    }

    /**
     * <p>
     * 重写角色与权限<br/>
     * 如果当前登陆的用户是 administrator或 是保护的用户，拥有所有的权限
     * </p>
     *
     * @see org.springframework.security.access.expression.SecurityExpressionRoot
     */
    protected static class ProtectedSecurityExpressionRoot implements SecurityExpressionOperations {

        protected final Authentication authentication;

        private AuthenticationTrustResolver trustResolver;

        private RoleHierarchy roleHierarchy;

        private Set<String> roles;

        private String defaultRolePrefix = "ROLE_";

        /**
         * Allows "permitAll" expression
         */
        public final boolean permitAll = true;

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
        public ProtectedSecurityExpressionRoot(Authentication authentication) {
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
         * @param authorities
         * @return
         * @see org.springframework.security.access.expression.SecurityExpressionRoot#hasAnyAuthority(String...)
         */
        @Override
        public boolean hasAnyAuthority(String... authorities) {
            UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
            return principal.isAdministrator()
                    || principal.isProtectUser()
                    || principal.getPermissionSet().containsAll(Arrays.asList(authorities))
                    || hasAnyAuthorityName(null, authorities);
        }

        @Override
        public final boolean hasRole(String role) {
            return hasAnyRole(role);
        }

        /**
         * @param roles
         * @return
         * @see org.springframework.security.access.expression.SecurityExpressionRoot#hasAnyRole(String...)
         */
        @Override
        public final boolean hasAnyRole(String... roles) {
            UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
            return principal.isAdministrator()
                    || principal.isProtectUser()
                    || CollectionUtils.containsAny(principal.getRoleSet(), Arrays.asList(roles))
                    || hasAnyAuthorityName(defaultRolePrefix, roles);
        }

        private boolean hasAnyAuthorityName(String prefix, String... roles) {
            Set<String> roleSet = getAuthoritySet();
            for (String role : roles) {
                String defaultedRole = getRoleWithDefaultPrefix(prefix, role);
                if (roleSet.contains(defaultedRole)) {
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


    /**
     * @see org.springframework.security.access.expression.method.MethodSecurityExpressionRoot
     */
    protected class ProtectedMethodSecurityExpressionRoot extends ProtectedSecurityExpressionRoot implements MethodSecurityExpressionOperations {

        private Object filterObject;

        private Object returnObject;

        private Object target;

        /**
         * Creates a new instance
         *
         * @param authentication the {@link Authentication} to use. Cannot be null.
         */
        public ProtectedMethodSecurityExpressionRoot(Authentication authentication) {
            super(authentication);
        }

        @Override
        public void setFilterObject(Object filterObject) {
            this.filterObject = filterObject;
        }

        @Override
        public Object getFilterObject() {
            return filterObject;
        }

        @Override
        public void setReturnObject(Object returnObject) {
            this.returnObject = returnObject;
        }

        @Override
        public Object getReturnObject() {
            return returnObject;
        }

        /**
         * Sets the "this" property for use in expressions. Typically this will be the "this"
         * property of the {@code JoinPoint} representing the method invocation which is being
         * protected.
         *
         * @param target the target object on which the method in is being invoked.
         */
        void setThis(Object target) {
            this.target = target;
        }

        @Override
        public Object getThis() {
            return target;
        }


    }
}
