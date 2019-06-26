package com.hk.core.authentication.oauth2.client.token.grant.code;

import com.hk.commons.util.ObjectUtils;
import com.hk.commons.util.StringUtils;
import com.hk.core.authentication.oauth2.AuthenticationType;
import com.hk.core.authentication.oauth2.LogoutParameter;
import com.hk.core.web.ServletContextHolder;
import lombok.Setter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.client.filter.state.DefaultStateKeyGenerator;
import org.springframework.security.oauth2.client.filter.state.StateKeyGenerator;
import org.springframework.security.oauth2.client.resource.OAuth2AccessDeniedException;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.resource.UserApprovalRequiredException;
import org.springframework.security.oauth2.client.resource.UserRedirectRequiredException;
import org.springframework.security.oauth2.client.token.*;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.common.exceptions.InvalidRequestException;
import org.springframework.security.oauth2.common.util.OAuth2Utils;
import org.springframework.security.web.PortMapper;
import org.springframework.security.web.PortMapperImpl;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.*;

/**
 * 重写 AuthorizationCode 认证模式，添加 logout_uri 请求参数
 *
 * @author huangkai
 * @date 2019-06-15 22:04
 * @see org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeAccessTokenProvider
 */
public class LogoutAuthorizationCodeAccessTokenProvider extends OAuth2AccessTokenSupport implements AccessTokenProvider, LogoutParameter {

    @Setter
    private StateKeyGenerator stateKeyGenerator = new DefaultStateKeyGenerator();

    @Setter
    private String scopePrefix = OAuth2Utils.SCOPE_PREFIX;

    @Setter
    private RequestEnhancer authorizationRequestEnhancer = new DefaultRequestEnhancer();

    @Setter
    private boolean stateMandatory = true;

    @Setter
    private boolean forceHttps = false;

    @Setter
    private String logoutUrl;

    @Setter
    private PortMapper portMapper = new PortMapperImpl();

    public boolean supportsResource(OAuth2ProtectedResourceDetails resource) {
        return resource instanceof AuthorizationCodeResourceDetails
                && AuthenticationType.authorization_code.name().equals(resource.getGrantType());
    }

    public boolean supportsRefresh(OAuth2ProtectedResourceDetails resource) {
        return supportsResource(resource);
    }

    public String obtainAuthorizationCode(OAuth2ProtectedResourceDetails details, AccessTokenRequest request)
            throws UserRedirectRequiredException, UserApprovalRequiredException, AccessDeniedException,
            OAuth2AccessDeniedException {

        AuthorizationCodeResourceDetails resource = (AuthorizationCodeResourceDetails) details;

        HttpHeaders headers = getHeadersForAuthorizationRequest(request);
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        if (request.containsKey(OAuth2Utils.USER_OAUTH_APPROVAL)) {
            form.set(OAuth2Utils.USER_OAUTH_APPROVAL, request.getFirst(OAuth2Utils.USER_OAUTH_APPROVAL));
            for (String scope : details.getScope()) {
                form.set(scopePrefix + scope, request.getFirst(OAuth2Utils.USER_OAUTH_APPROVAL));
            }
        } else {
            form.putAll(getParametersForAuthorizeRequest(resource, request));
        }
        authorizationRequestEnhancer.enhance(request, resource, form, headers);
        final AccessTokenRequest copy = request;

        final ResponseExtractor<ResponseEntity<Void>> delegate = getAuthorizationResponseExtractor();
        ResponseExtractor<ResponseEntity<Void>> extractor = response -> {
            if (response.getHeaders().containsKey("Set-Cookie")) {
                copy.setCookie(response.getHeaders().getFirst("Set-Cookie"));
            }
            return delegate.extractData(response);
        };
        // Instead of using restTemplate.exchange we use an explicit response extractor here so it can be overridden by
        // subclasses
        ResponseEntity<Void> response = getRestTemplate().execute(resource.getUserAuthorizationUri(), HttpMethod.POST,
                getRequestCallback(resource, form, headers), extractor, form.toSingleValueMap());
        if (response.getStatusCode() == HttpStatus.OK) {
            // Need to re-submit with approval...
            throw getUserApprovalSignal(resource, request);
        }
        URI location = response.getHeaders().getLocation();
        String query = location.getQuery();
        Map<String, String> map = OAuth2Utils.extractMap(query);
        if (map.containsKey("state")) {
            request.setStateKey(map.get("state"));
            if (request.getPreservedState() == null) {
                String redirectUri = resource.getRedirectUri(request);
                if (redirectUri != null) {
                    request.setPreservedState(redirectUri);
                } else {
                    request.setPreservedState(new Object());
                }
            }
        }
        String code = map.get("code");
        if (code == null) {
            throw new UserRedirectRequiredException(location.toString(), form.toSingleValueMap());
        }
        request.set("code", code);
        return code;
    }

    protected ResponseExtractor<ResponseEntity<Void>> getAuthorizationResponseExtractor() {
        return response -> new ResponseEntity<>(response.getHeaders(), response.getStatusCode());
    }

    public OAuth2AccessToken obtainAccessToken(OAuth2ProtectedResourceDetails details, AccessTokenRequest request)
            throws UserRedirectRequiredException, UserApprovalRequiredException, AccessDeniedException,
            OAuth2AccessDeniedException {

        AuthorizationCodeResourceDetails resource = (AuthorizationCodeResourceDetails) details;

        if (request.getAuthorizationCode() == null) {
            if (request.getStateKey() == null) {
                throw getRedirectForAuthorization(resource, request);
            }
            obtainAuthorizationCode(resource, request);
        }
        return retrieveToken(request, resource, getParametersForTokenRequest(resource, request),
                getHeadersForTokenRequest(request));

    }

    public OAuth2AccessToken refreshAccessToken(OAuth2ProtectedResourceDetails resource,
                                                OAuth2RefreshToken refreshToken, AccessTokenRequest request) throws UserRedirectRequiredException,
            OAuth2AccessDeniedException {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        String refresh_token = AuthenticationType.refresh_token.name();
        form.add(OAuth2Utils.GRANT_TYPE, refresh_token);
        form.add(refresh_token, refreshToken.getValue());
        try {
            return retrieveToken(request, resource, form, getHeadersForTokenRequest(request));
        } catch (OAuth2AccessDeniedException e) {
            throw getRedirectForAuthorization((AuthorizationCodeResourceDetails) resource, request);
        }
    }

    private HttpHeaders getHeadersForTokenRequest(AccessTokenRequest request) {
        HttpHeaders headers = new HttpHeaders();
        // No cookie for token request
        return headers;
    }

    private HttpHeaders getHeadersForAuthorizationRequest(AccessTokenRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.putAll(request.getHeaders());
        if (request.getCookie() != null) {
            headers.set("Cookie", request.getCookie());
        }
        return headers;
    }

    private MultiValueMap<String, String> getParametersForTokenRequest(AuthorizationCodeResourceDetails resource,
                                                                       AccessTokenRequest request) {

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.set(OAuth2Utils.GRANT_TYPE, AuthenticationType.authorization_code.name());
        form.set("code", request.getAuthorizationCode());

        Object preservedState = request.getPreservedState();
        if (request.getStateKey() != null || stateMandatory) {
            // The token endpoint has no use for the state so we don't send it back, but we are using it
            // for CSRF detection client side...
            if (preservedState == null) {
                throw new InvalidRequestException(
                        "Possible CSRF detected - state parameter was required but no state could be found");
            }
        }

        // Extracting the redirect URI from a saved request should ignore the current URI, so it's not simply a call to
        // resource.getRedirectUri()
        String redirectUri;
        // Get the redirect uri from the stored state
        if (preservedState instanceof String) {
            // Use the preserved state in preference if it is there
            // TODO: treat redirect URI as a special kind of state (this is a historical mini hack)
            redirectUri = String.valueOf(preservedState);
        } else {
            redirectUri = resource.getRedirectUri(request);
        }

        if (redirectUri != null && !"NONE".equals(redirectUri)) {
            form.set(OAuth2Utils.REDIRECT_URI, redirectUri);
        }

        return form;

    }

    private MultiValueMap<String, String> getParametersForAuthorizeRequest(AuthorizationCodeResourceDetails resource,
                                                                           AccessTokenRequest request) {

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.set(OAuth2Utils.RESPONSE_TYPE, "code");
        form.set(OAuth2Utils.CLIENT_ID, resource.getClientId());

        if (request.get("scope") != null) {
            form.set("scope", request.getFirst("scope"));
        } else {
            form.set("scope", OAuth2Utils.formatParameterList(resource.getScope()));
        }

        // Extracting the redirect URI from a saved request should ignore the current URI, so it's not simply a call to
        // resource.getRedirectUri()
        String redirectUri = resource.getPreEstablishedRedirectUri();

        Object preservedState = request.getPreservedState();
        if (redirectUri == null && preservedState != null) {
            // no pre-established redirect uri: use the preserved state
            // TODO: treat redirect URI as a special kind of state (this is a historical mini hack)
            redirectUri = String.valueOf(preservedState);
        } else {
            redirectUri = request.getCurrentUri();
        }

        String stateKey = request.getStateKey();
        if (stateKey != null) {
            form.set("state", stateKey);
            if (preservedState == null) {
                throw new InvalidRequestException(
                        "Possible CSRF detected - state parameter was present but no state could be found");
            }
        }
        if (redirectUri != null) {
            form.set(OAuth2Utils.REDIRECT_URI, redirectUri);
        }
        return form;

    }

    private UserRedirectRequiredException getRedirectForAuthorization(AuthorizationCodeResourceDetails resource,
                                                                      AccessTokenRequest request) {

        // we don't have an authorization code yet. So first get that.
        TreeMap<String, String> requestParameters = new TreeMap<>();
        requestParameters.put(OAuth2Utils.RESPONSE_TYPE, "code"); // oauth2 spec, section 3
        requestParameters.put(OAuth2Utils.CLIENT_ID, resource.getClientId());
        // Client secret is not required in the initial authorization request

        String redirectUri = resource.getRedirectUri(request);
        if (redirectUri != null) {

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(redirectUri);
            UriComponents url = builder.cloneBuilder().build();
            String scheme = url.getScheme();
            int port = url.getPort();
            if (forceHttps) {
                scheme = "https";
                port = ObjectUtils.defaultIfNull(portMapper.lookupHttpPort(url.getPort()), url.getPort());
                redirectUri = builder.cloneBuilder().scheme(scheme).port(port).toUriString();
            }
            requestParameters.put(OAuth2Utils.REDIRECT_URI, redirectUri);

            if (StringUtils.isNotEmpty(logoutUrl)) {
                String contextPath = ServletContextHolder.getContextPath();
                UriComponentsBuilder logoutUrlBuilder = UriComponentsBuilder.newInstance()
                        .scheme(scheme)
                        .host(url.getHost())
                        .port(port)
                        .path(StringUtils.equals("/", contextPath) ? StringUtils.EMPTY : contextPath + logoutUrl);
                requestParameters.put(LOGOUT_PARAMETER_NAME, logoutUrlBuilder.toUriString());
            }
        }

        if (resource.isScoped()) {
            StringBuilder builder = new StringBuilder();
            List<String> scope = resource.getScope();
            if (scope != null) {
                Iterator<String> scopeIt = scope.iterator();
                while (scopeIt.hasNext()) {
                    builder.append(scopeIt.next());
                    if (scopeIt.hasNext()) {
                        builder.append(' ');
                    }
                }
            }
            requestParameters.put(OAuth2Utils.SCOPE, builder.toString());
        }


        UserRedirectRequiredException redirectException = new UserRedirectRequiredException(
                resource.getUserAuthorizationUri(), requestParameters);

        String stateKey = stateKeyGenerator.generateKey(resource);
        redirectException.setStateKey(stateKey);
        request.setStateKey(stateKey);
        redirectException.setStateToPreserve(redirectUri);
        request.setPreservedState(redirectUri);

        return redirectException;

    }

    protected UserApprovalRequiredException getUserApprovalSignal(AuthorizationCodeResourceDetails resource,
                                                                  AccessTokenRequest request) {
        String message = String.format("Do you approve the client '%s' to access your resources with scope=%s",
                resource.getClientId(), resource.getScope());
        return new UserApprovalRequiredException(resource.getUserAuthorizationUri(), Collections.singletonMap(
                OAuth2Utils.USER_OAUTH_APPROVAL, message), resource.getClientId(), resource.getScope());
    }

}
