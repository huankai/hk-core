package com.hk.core.authentication.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;

import com.hk.core.authentication.api.SecurityContext;
import com.hk.core.authentication.api.UserPrincipal;

/**
 * 
 * @author: kevin
 *
 */
public class ShiroSecurityContext implements SecurityContext {

	/* (non-Javadoc)
	 * @see com.hk.core.authentication.api.SecurityContext#getPrincipal()
	 */
	@Override
	public UserPrincipal getPrincipal() {
		return SecurityUtils.getSubject().getPrincipals().oneByType(UserPrincipal.class);
	}

	/* (non-Javadoc)
	 * @see com.hk.core.authentication.api.SecurityContext#isAuthenticated()
	 */
	@Override
	public boolean isAuthenticated() {
		return SecurityUtils.getSubject().isAuthenticated();
	}

	/* (non-Javadoc)
	 * @see com.hk.core.authentication.api.SecurityContext#setSessionAttribute(java.lang.Object, java.lang.Object, boolean)
	 */
	@Override
	public void setSessionAttribute(String key, Object value, boolean create) {
		Session session = SecurityUtils.getSubject().getSession(create);
		if (null != session) {
			session.setAttribute(key, value);
		}
	}

	/* (non-Javadoc)
	 * @see com.hk.core.authentication.api.SecurityContext#getSessionAttribute(java.lang.Object)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public <T> T getSessionAttribute(String key) {
		return (T) SecurityUtils.getSubject().getSession().getAttribute(key);
	}

	/* (non-Javadoc)
	 * @see com.hk.core.authentication.api.SecurityContext#removeSessionAttribute(java.lang.Object)
	 */
	@Override
	public void removeSessionAttribute(String key) {
		Session session = SecurityUtils.getSubject().getSession(false);
		if (null != session) {
			session.removeAttribute(key);
		}
	}
	
}
