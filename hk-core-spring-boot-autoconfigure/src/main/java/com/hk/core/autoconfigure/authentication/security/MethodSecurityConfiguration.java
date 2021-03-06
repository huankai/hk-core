package com.hk.core.autoconfigure.authentication.security;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.access.intercept.RunAsManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.core.Authentication;

import com.hk.core.authentication.security.expression.AdminAccessMethodSecurityExpressionRoot;
import com.hk.core.authentication.security.expression.AdminAccessPermissionEvaluator;

/**
 * 开启注解权限控制
 *
 * @author kevin
 * @date 2018-05-16 15:57
 */
@Configuration
@ConditionalOnClass(AdminAccessMethodSecurityExpressionRoot.class)
@EnableGlobalMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
public class MethodSecurityConfiguration extends GlobalMethodSecurityConfiguration {

	@Override
	protected MethodSecurityExpressionHandler createExpressionHandler() {
		return new DefaultMethodSecurityExpressionHandler() {
			@Override
			protected MethodSecurityExpressionOperations createSecurityExpressionRoot(Authentication authentication,
					MethodInvocation invocation) {
				var root = new AdminAccessMethodSecurityExpressionRoot(authentication);
				root.setThis(invocation.getThis());
				root.setPermissionEvaluator(new AdminAccessPermissionEvaluator());
				root.setTrustResolver(getTrustResolver());
				root.setRoleHierarchy(getRoleHierarchy());
				root.setDefaultRolePrefix(getDefaultRolePrefix());
				return root;
			}
		};
	}

	@Override
	protected RunAsManager runAsManager() {
		return super.runAsManager();
	}
}
