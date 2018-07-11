package com.hk.core.authentication.shiro.config;

import java.util.Collection;
import java.util.List;

import org.apache.shiro.mgt.SessionsSecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.collect.Lists;
import com.hk.core.authentication.shiro.listeners.LogSessionListener;

/**
 * Shiro配置
 * 
 * @author: kevin
 * @date 2017年10月22日下午1:54:02
 */
@Configuration
public class ShiroWebAutoConfiguration
		extends org.apache.shiro.spring.config.web.autoconfigure.ShiroWebAutoConfiguration {

	/**
	 * 会话30分钟 (毫秒)
	 */
	@Value("#{@environment['shiro.sessionManager.globalSessionTimeout'] ?: 3600000 }")
	protected long globalSessionTimeout;

//	@Bean
//	@Override
//	@ConditionalOnClass(Pac4jSubjectFactory.class)
//	protected SubjectFactory subjectFactory() {
//		return new Pac4jSubjectFactory();
//	}

	/* ********************* Shiro Configuration ************************* */

	/**
	 * 
	 * @param realms
	 * @return
	 */
	@Bean
	@Override
	public SessionsSecurityManager securityManager(List<Realm> realms) {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager(realms);
		securityManager.setSubjectFactory(subjectFactory());
		securityManager.setSessionManager(sessionManager());
		return securityManager;
	}

	/**
	 * 
	 * @return
	 */
	@Bean
	@Override
	public SessionManager sessionManager() {
		DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
		sessionManager.setSessionDAO(sessionDAO());
		sessionManager.setGlobalSessionTimeout(globalSessionTimeout);
		sessionManager.setSessionValidationSchedulerEnabled(false);
		sessionManager.setSessionListeners(sessionListeners());
		sessionManager.setSessionIdUrlRewritingEnabled(sessionIdUrlRewritingEnabled);
		return sessionManager;
	}

	/**
	 * 
	 * @return
	 */
	@Bean
	public Collection<SessionListener> sessionListeners() {
		Collection<SessionListener> listeners = Lists.newArrayList(new LogSessionListener());
		return listeners;
	}

}
