package com.hk.core.authentication.security.authentication.filters;

import com.hk.commons.util.StringUtils;
import com.hk.core.authentication.api.validatecode.ValidateCodeException;
import com.hk.core.authentication.api.validatecode.ValidateCodeProcessor;
import com.hk.core.web.Webs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 验证码验证
 *
 * @author kevin
 * @date 2018-07-26 15:35
 */
public class ValidateCodeFilter extends OncePerRequestFilter {

	private static final Logger LOGGER = LoggerFactory.getLogger(ValidateCodeFilter.class);

	/**
	 * 验证处理器
	 */
	private final ValidateCodeProcessor validateCodeProcessor;

	private boolean postOnly = true;

	private String processingUri;

	private AuthenticationFailureHandler authenticationFailureHandler = new SimpleUrlAuthenticationFailureHandler(
			"/login?error");

	/**
	 * @param validateCodeProcessor 验证处理器
	 * @param processingUri         处理URI
	 */
	public ValidateCodeFilter(ValidateCodeProcessor validateCodeProcessor, String processingUri) {
		this.validateCodeProcessor = validateCodeProcessor;
		if (!StringUtils.startsWith(processingUri, "/")) {
			processingUri = "/" + processingUri;
		}
		this.processingUri = processingUri;
	}

	public void setPostOnly(boolean postOnly) {
		this.postOnly = postOnly;
	}

	public void setAuthenticationFailureHandler(AuthenticationFailureHandler authenticationFailureHandler) {
		this.authenticationFailureHandler = authenticationFailureHandler;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {
		String method = request.getMethod().toUpperCase();
		String requestUri = Webs.getClearContextPathUri(request);
		if (StringUtils.equals(requestUri, processingUri)) {
			if (postOnly && StringUtils.notEquals(method, "POST")) {
				throw new AuthenticationServiceException("Authentication method not supported: " + method);
			}
			try {
				validate(new ServletWebRequest(request, response));
			} catch (ValidateCodeException e) {
				LOGGER.error(e.getMessage(), e);
				authenticationFailureHandler.onAuthenticationFailure(request, response,
						new AuthenticationServiceException(e.getMessage(), e));
				return;
			}
		}
		filterChain.doFilter(request, response);
	}

	private void validate(ServletWebRequest request) throws ValidateCodeException {
		validateCodeProcessor.validate(request);
	}
}
