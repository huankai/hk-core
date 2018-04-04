package com.hk.core.audit;

import org.springframework.data.domain.AuditorAware;

import com.hk.core.authentication.api.SecurityContext;

/**
 * jpa 审计功能
 * 
 * @author huangkai
 *
 */
public class UserIdAuditor implements AuditorAware<String> {

	private SecurityContext securityContext;

	@Override
	public String getCurrentAuditor() {
		return securityContext.getPrincipal().getUserId();
	}

	public void setSecurityContext(SecurityContext securityContext) {
		this.securityContext = securityContext;
	}

}
