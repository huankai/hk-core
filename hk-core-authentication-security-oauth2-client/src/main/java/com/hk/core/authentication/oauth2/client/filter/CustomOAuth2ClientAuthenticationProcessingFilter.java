package com.hk.core.authentication.oauth2.client.filter;

import com.hk.commons.JsonResult;
import com.hk.core.authentication.security.handler.login.LoginAuthenticationFailureHandler;
import com.hk.core.web.Webs;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.filter.OAuth2AuthenticationFailureEvent;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetailsSource;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Objects;

/**
 * 重写 OAuth2ClientAuthenticationProcessingFilter，认证成功后，将token 信息返回，并销毁 session 中的 token 信息
 *
 * @author kevin
 * @date 2020-02-14 12:23
 * @see org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter
 */
@Deprecated
public class CustomOAuth2ClientAuthenticationProcessingFilter extends GenericFilterBean
        implements ApplicationEventPublisherAware {

    private final RequestMatcher requiresAuthenticationRequestMatcher;

    @Setter
    public OAuth2RestOperations restTemplate;

    @Setter
    private ResourceServerTokenServices tokenServices;

    private ApplicationEventPublisher eventPublisher;

    @Setter
    private AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource = new OAuth2AuthenticationDetailsSource();

    private AuthenticationFailureHandler failureHandler = new LoginAuthenticationFailureHandler();

    public CustomOAuth2ClientAuthenticationProcessingFilter(String filterProcessesUrl) {
        this.requiresAuthenticationRequestMatcher = new AntPathRequestMatcher(filterProcessesUrl);
    }

    /**
     * session 中存储的 oauth2ClientContext
     */
    private static final String SESSION_SCOPED_OAUTH2_CLIENT_CONTEXT_KEY = "scopedTarget.oauth2ClientContext";

    private static final String SESSION_OAUTH2_CLIENT_CONTEXT_KEY = "oauth2ClientContext";

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        if (!requiresAuthentication(request)) {
            chain.doFilter(request, response);

            return;
        }
        Authentication authResult;
        try {
            authResult = attemptAuthentication(request, response);
            if (authResult == null) {
                // return immediately as subclass has indicated that it hasn't completed
                // authentication
                return;
            }
        } catch (InternalAuthenticationServiceException failed) {
            logger.error("An internal error occurred while trying to authenticate the user.", failed);
            unsuccessfulAuthentication(request, response, failed);
            return;
        } catch (AuthenticationException failed) {
            // Authentication failed
            unsuccessfulAuthentication(request, response, failed);
            return;
        }
        successfulAuthentication(request, response, chain, authResult);

    }

    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        OAuth2AccessToken accessToken;
        try {
            accessToken = restTemplate.getAccessToken();
        } catch (OAuth2Exception e) {
            BadCredentialsException bad = new BadCredentialsException("Could not obtain access token", e);
            publish(new OAuth2AuthenticationFailureEvent(bad));
            throw bad;
        }
        try {
            OAuth2Authentication result = tokenServices.loadAuthentication(accessToken.getValue());
            if (authenticationDetailsSource != null) {
                request.setAttribute(OAuth2AuthenticationDetails.ACCESS_TOKEN_VALUE, accessToken.getValue());
                request.setAttribute(OAuth2AuthenticationDetails.ACCESS_TOKEN_TYPE, accessToken.getTokenType());
                result.setDetails(authenticationDetailsSource.buildDetails(request));
            }
            publish(new AuthenticationSuccessEvent(result));
            return result;
        } catch (InvalidTokenException e) {
            BadCredentialsException bad = new BadCredentialsException("Could not obtain user details from token", e);
            publish(new OAuth2AuthenticationFailureEvent(bad));
            throw bad;
        }

    }

    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) {

        if (logger.isDebugEnabled()) {
            logger.debug("Authentication success. Updating SecurityContextHolder to contain: "
                    + authResult);
        }
        SecurityContextHolder.getContext().setAuthentication(authResult);
        // Fire event
        if (this.eventPublisher != null) {
            eventPublisher.publishEvent(new InteractiveAuthenticationSuccessEvent(
                    authResult, this.getClass()));
        }
        restTemplate.getAccessToken();
        DefaultOAuth2ClientContext auth2ClientContext = Webs.getAttributeFromSession(SESSION_SCOPED_OAUTH2_CLIENT_CONTEXT_KEY, DefaultOAuth2ClientContext.class);
        try {
            Webs.writeJson(response, HttpServletResponse.SC_OK, new JsonResult<>(auth2ClientContext.getAccessToken().getValue()));
        } finally {
//            /* 认证成功后，删除 token中的信息　*/
            Webs.removeAttributeFromSession(SESSION_SCOPED_OAUTH2_CLIENT_CONTEXT_KEY);
            Webs.removeAttributeFromSession(SESSION_OAUTH2_CLIENT_CONTEXT_KEY);
            HttpSession session = request.getSession(false);
            if (Objects.nonNull(session)) {
                session.invalidate();
            }
        }

    }

    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response, AuthenticationException failed)
            throws IOException, ServletException {
        SecurityContextHolder.clearContext();

        if (logger.isDebugEnabled()) {
            logger.debug("Authentication request failed: " + failed.toString(), failed);
            logger.debug("Updated SecurityContextHolder to contain null Authentication");
            logger.debug("Delegating to authentication failure handler " + failureHandler);
        }
        failureHandler.onAuthenticationFailure(request, response, failed);
    }

    private boolean requiresAuthentication(HttpServletRequest request) {
        return requiresAuthenticationRequestMatcher.matches(request);
    }

    private void publish(ApplicationEvent event) {
        if (eventPublisher != null) {
            eventPublisher.publishEvent(event);
        }
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.eventPublisher = applicationEventPublisher;
    }
}
