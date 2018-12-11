package com.hk.core.authentication.security.authentication.email;

import com.hk.commons.util.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
   *   邮箱登陆过滤器
 *
 * @author: kevin
 * @date: 2018-07-26 16:21
 */
public class EmailAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	private final String emailParameter;

	private final boolean postOnly;

	public EmailAuthenticationFilter(String emailParameter, String requestUrl, boolean postOnly) {
		super(new AntPathRequestMatcher(requestUrl, HttpMethod.POST.name()));
		this.emailParameter = emailParameter;
		this.postOnly = postOnly;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		if (postOnly && StringUtils.notEquals(HttpMethod.POST.name(), request.getMethod())) {
			throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
		}
		String mobile = StringUtils.trimToEmpty(obtainEmail(request));
		EmailAuthenticationToken authenticationToken = new EmailAuthenticationToken(mobile);
		setDetails(request, authenticationToken);
		return getAuthenticationManager().authenticate(authenticationToken);
	}

	private String obtainEmail(HttpServletRequest request) {
		return request.getParameter(emailParameter);
	}

	private void setDetails(HttpServletRequest request, EmailAuthenticationToken authRequest) {
		authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
	}
}
