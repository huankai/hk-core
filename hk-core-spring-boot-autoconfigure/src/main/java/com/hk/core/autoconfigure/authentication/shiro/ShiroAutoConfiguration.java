package com.hk.core.autoconfigure.authentication.shiro;

import com.hk.core.authentication.api.SecurityContext;
import com.hk.core.authentication.shiro.ShiroSecurityContext;
import com.hk.core.authentication.shiro.UserDetailsService;
import com.hk.core.authentication.shiro.realm.HashedAuthorizingRealm;
import com.hk.core.authentication.shiro.realm.ScryptAuthorizingRealm;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.pam.AuthenticationStrategy;
import org.apache.shiro.authc.pam.FirstSuccessfulStrategy;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroWebConfiguration;
import org.apache.shiro.spring.web.config.ShiroWebFilterConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Shiro Auto configuration
 *
 * @author huangkai
 * @date 2018-12-17 15:23
 */
@Configuration
@ConditionalOnClass(value = {SecurityUtils.class})
@EnableConfigurationProperties(value = {ShiroProperties.class})
@Import(value = {ShiroWebFilterConfiguration.class})
public class ShiroAutoConfiguration extends ShiroWebConfiguration {

    private ShiroProperties shiroProperties;

    public ShiroAutoConfiguration(ShiroProperties shiroProperties) {
        this.shiroProperties = shiroProperties;
    }

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
    public List<Realm> realm(UserDetailsService userDetailsService) {
        List<Realm> list = new ArrayList<>();
        list.add(new ScryptAuthorizingRealm(userDetailsService));
        list.add(new HashedAuthorizingRealm("MD5", userDetailsService));
        return list;
    }

    @Bean
    public ShiroFilterChainDefinition shiroFilterChainDefinition() {
        DefaultShiroFilterChainDefinition chainDefinition = new DefaultShiroFilterChainDefinition();
        Map<String, String> filterChainDefinitionMap = shiroProperties.getFilterChainDefinitionMap();
        chainDefinition.addPathDefinitions(filterChainDefinitionMap);
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


    /**
     * 只要有一个 Realm 认证成功即可，只返回第一个Realm 身份验证成功的信息，其它的会忽略
     *
     * @return {@link AuthenticationStrategy}
     */
    @Override
    protected AuthenticationStrategy authenticationStrategy() {
        return new FirstSuccessfulStrategy();
    }
}
