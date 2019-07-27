package com.hk.core.autoconfigure.data;

import com.hk.core.authentication.api.SecurityContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

/**
 * Jpa、JDBC auditor
 *
 * @author kevin
 * @date 2018-10-10 10:11
 */
@Configuration
@ConditionalOnClass(value = {AuditorAware.class, SecurityContext.class})
public class AuditorConfiguration {

    @Bean
    @ConditionalOnMissingBean(value = AuditorAware.class)
    public AuditorAware<Long> userIdAuditor(SecurityContext securityContext) {
        return new UserAuditorAware(securityContext);
    }

    private class UserAuditorAware implements AuditorAware<Long> {

        private SecurityContext securityContext;

        private UserAuditorAware(SecurityContext securityContext) {
            this.securityContext = securityContext;
        }

        @Override
        public Optional<Long> getCurrentAuditor() {
            return Optional.of(securityContext.isAuthenticated() ? securityContext.getPrincipal().getUserId()
                    : 0L);
        }
    }
}
