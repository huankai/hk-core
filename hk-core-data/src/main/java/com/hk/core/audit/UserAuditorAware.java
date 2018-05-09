package com.hk.core.audit;

import com.hk.core.authentication.api.SecurityContext;
import org.springframework.data.domain.AuditorAware;

/**
 * jpa 审计功能
 *
 * @author huangkai
 */
public class UserAuditorAware implements AuditorAware<String> {

    private final SecurityContext securityContext;

    public UserAuditorAware(SecurityContext securityContext) {
        this.securityContext = securityContext;
    }

    @Override
    public String getCurrentAuditor() {
        return securityContext.getPrincipal().getUserId();
    }

}
