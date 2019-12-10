package com.hk.core.autoconfigure.authentication.security;

import com.hk.commons.util.ArrayUtils;
import com.hk.commons.util.CollectionUtils;
import com.hk.core.authentication.api.PermitMatcher;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;

import java.util.Set;

/**
 * @author huangkai
 * @date 2019/3/12 9:16
 */
public abstract class HttpSecurityUtils {

    public static void buildPermitMatchers(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry urlRegistry,
                                           Set<PermitMatcher> permitAllMatchers) {
        if (CollectionUtils.isNotEmpty(permitAllMatchers)) {
            for (var permitMatcher : permitAllMatchers) {
                if (ArrayUtils.isNotEmpty(permitMatcher.getPermissions())) {
                    urlRegistry.antMatchers(permitMatcher.getMethod(), permitMatcher.getUris()).hasAnyAuthority(permitMatcher.getPermissions());
                } else if (ArrayUtils.isNotEmpty(permitMatcher.getRoles())) {
                    urlRegistry.antMatchers(permitMatcher.getMethod(), permitMatcher.getUris()).hasAnyRole(permitMatcher.getRoles());
                } else {
                    urlRegistry.antMatchers(permitMatcher.getMethod(), permitMatcher.getUris()).permitAll();
                }
            }
        }
    }
}
