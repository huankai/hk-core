package com.hk.core.autoconfigure.authentication.shiro;

import com.hk.core.authentication.api.SecurityContext;
import com.hk.core.authentication.shiro.ShiroSecurityContext;
import com.hk.core.authentication.shiro.UserDetailsService;
import com.hk.core.authentication.shiro.realm.CustomAuthorizingRealm;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroWebConfiguration;
import org.apache.shiro.spring.web.config.ShiroWebFilterConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Shiro Auto configuration
 *
 * @author huangkai
 * @date 2018-12-17 15:23
 */
@Configuration
@ConditionalOnClass(value = {SecurityUtils.class})
@Import(value = {ShiroWebConfiguration.class,ShiroWebFilterConfiguration.class})
public class ShiroAutoConfiguration {

    /**
     * @return {@link SecurityContext}
     */
    @Bean
    public SecurityContext securityContext() {
        return new ShiroSecurityContext();
    }

    /**
     * Shiro Realm
     *
     * @param userDetailsService {@link UserDetailsService}
     * @return Shiro realm
     */
    @Bean
    @ConditionalOnMissingBean(Realm.class)
    public Realm realm(UserDetailsService userDetailsService) {
        return new CustomAuthorizingRealm(userDetailsService);
    }

    @Bean
    public ShiroFilterChainDefinition shiroFilterChainDefinition() {
        DefaultShiroFilterChainDefinition chainDefinition = new DefaultShiroFilterChainDefinition();

        // logged in users with the 'admin' role
        chainDefinition.addPathDefinition("/admin/**", "authc, roles[admin]");

        // logged in users with the 'document:read' permission
        chainDefinition.addPathDefinition("/docs/**", "authc, perms[document:read]");

        chainDefinition.addPathDefinition("/login", "anon");
        // all other paths require a logged in user
        chainDefinition.addPathDefinition("/**", "authc");
        return chainDefinition;
    }


    /**
     * 缓存管理
     *
     * @return {@link CacheManager}
     */
    @Bean
    public CacheManager cacheManager() {
        return new MemoryConstrainedCacheManager();
    }

}
