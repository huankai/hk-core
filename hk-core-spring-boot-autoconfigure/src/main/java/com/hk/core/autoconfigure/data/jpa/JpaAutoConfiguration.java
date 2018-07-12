package com.hk.core.autoconfigure.data.jpa;

import com.hk.core.authentication.api.SecurityContext;
import com.hk.core.data.jpa.BaseJpaRepositoryFactoryBean;
import com.hk.core.data.jpa.repository.BaseRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Optional;

/**
 * <p>
 * JpaAutoConfiguration
 * </p>
 * <p>
 * EnableJpaRepositories
 * basePackages 指定扫描包，所有 repository 都必须放在指定的目录下
 * </p>
 *
 * @author: kevin
 * @date 2018-06-07 13:07
 */
@EnableJpaAuditing
@ConditionalOnClass(BaseRepository.class)
@EnableJpaRepositories(basePackages = {"com.hk.**.repository", "com.hk.**.dao"}, repositoryFactoryBeanClass = BaseJpaRepositoryFactoryBean.class)
public class JpaAutoConfiguration {
    /**
     * jpa audit功能
     *
     * @return
     */
    @Bean
    @ConditionalOnClass(value = {SecurityContext.class})
    @ConditionalOnMissingBean(value = AuditorAware.class)
    public AuditorAware<?> userIdAuditor(SecurityContext securityContext) {
        return new UserAuditorAware(securityContext);
    }

    private class UserAuditorAware implements AuditorAware<String> {

        private SecurityContext securityContext;

        private UserAuditorAware(SecurityContext securityContext) {
            this.securityContext = securityContext;
        }

        @Override
        public Optional<String> getCurrentAuditor() {
            return Optional.of(securityContext.getPrincipal().getUserId());
        }
    }


}
