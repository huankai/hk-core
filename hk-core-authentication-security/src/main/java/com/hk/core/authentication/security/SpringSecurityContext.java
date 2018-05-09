package com.hk.core.authentication.security;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.hk.commons.util.StringUtils;
import com.hk.core.authentication.api.SecurityContext;
import com.hk.core.authentication.api.UserPrincipal;
import com.hk.core.web.Webs;

/**
 * 
 * @author huangkai
 *
 */
public class SpringSecurityContext implements SecurityContext {


	@Override
	public UserPrincipal getPrincipal() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return (UserPrincipal) authentication.getPrincipal();
	}

	@Override
	public boolean isAuthenticated() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return null != authentication && !(authentication instanceof AnonymousAuthenticationToken)
				&& authentication.isAuthenticated();
	}

	@Override
	public void setSessionAttribute(String key, Object value, boolean create) {
		if (StringUtils.isNotEmpty(key)) {
			Webs.setAttributeFromSession(key, value, create);
		}
	}

	@Override
	public <T> T getSessionAttribute(String key) {
		return StringUtils.isEmpty(key) ? null : Webs.getAttributeFromSession(key);
	}

	@Override
	public void removeSessionAttribute(String key) {
		if (StringUtils.isNotEmpty(key)) {
			Webs.removeAttributeFromSession(key);
		}
	}

}
