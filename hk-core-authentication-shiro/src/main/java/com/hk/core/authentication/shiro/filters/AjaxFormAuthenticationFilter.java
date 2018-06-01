package com.hk.core.authentication.shiro.filters;

import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;


/**
 * 支持Ajax请求登陆
 * 
 * @author huangkai
 * @date 2017年10月25日上午10:02:18
 */
public class AjaxFormAuthenticationFilter extends FormAuthenticationFilter {

//	private static final String DEFAULT_LOGIN_FAILURE_MESSAGE_ATTRIBUTE_NAME = "loginFailureMessage";
//
//    private String loginFailureMessageAttribute = DEFAULT_LOGIN_FAILURE_MESSAGE_ATTRIBUTE_NAME;
//
//	private static final String DEFAULT_LOGIN_URL = "/login";
//
//	/**
//	 * 如果设置为true,登陆成功后返回登陆之前的请求地址，否则总是返回主页
//	 */
//	private boolean saveRequestedUrlEnabled = true;
//
//	public AjaxFormAuthenticationFilter() {
//        setLoginUrl(DEFAULT_LOGIN_URL);
//	}
//
//	@Override
//	public boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
//		boolean accessAllowed = isAccessAllowed(request, response, mappedValue);
//		if (accessAllowed) {
//			if (isLoginRequest(request, response)) {
//				return onLoginSuccessIntenal(request, response);
//			}
//		}
//		return accessAllowed || onAccessDenied(request, response, mappedValue);
//	}
//
//	@Override
//	protected void saveRequest(ServletRequest request) {
//		if (isSaveRequestedUrlEnabled()) {
//			super.saveRequest(request);
//		}
//	}
//
//	@Override
//	protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request,
//			ServletResponse response) throws Exception {
//		return onLoginSuccessIntenal(request, response);
//	}
//
//	private boolean onLoginSuccessIntenal(ServletRequest request, ServletResponse response) throws Exception {
//		HttpServletRequest req = (HttpServletRequest) request;
//		HttpServletResponse resp = (HttpServletResponse) response;
//		if (Webs.isAjax(req)) {
//			String successUrl = getSaveRequestUrl(req, resp, getSuccessUrl());
//			Webs.writeJson(resp, HttpServletResponse.SC_OK, new JsonResult(Status.SUCCESS, "登陆成功", successUrl));
//		} else {
//			issueSuccessRedirect(request, response);
//		}
//		return false;
//	}
//
//	@Override
//	protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request,
//			ServletResponse response) {
//		HttpServletRequest req = (HttpServletRequest) request;
//		HttpServletResponse resp = (HttpServletResponse) response;
//		if (Webs.isAjax(req)) {
//			Webs.writeJson(resp, HttpServletResponse.SC_UNAUTHORIZED, new JsonResult(false, "用户名或密码不正确"));
//		} else {
//			setFailureAttribute(request, e);
//		}
//		return true;
//	}
//
//	@Override
//	protected void setFailureAttribute(ServletRequest request, AuthenticationException ae) {
//		request.setAttribute(getLoginFailureMessageAttribute(), ae.getMessage());
//		super.setFailureAttribute(request, ae);
//	}
//
//	private String getSaveRequestUrl(HttpServletRequest request, HttpServletResponse response, String fallbackUrl) {
//		String successUrl = null;
//		boolean contextRelative = true;
//		SavedRequest savedRequest = WebUtils.getAndClearSavedRequest(request);
//		if (savedRequest != null && savedRequest.getMethod().equalsIgnoreCase(AccessControlFilter.GET_METHOD)) {
//			successUrl = savedRequest.getRequestUrl();
//			contextRelative = false;
//		}
//
//		if (successUrl == null) {
//			successUrl = fallbackUrl;
//		}
//
//		if (successUrl == null) {
//			throw new IllegalStateException("Success URL not available via saved request or via the "
//					+ "successUrlFallback method parameter. One of these must be non-null for "
//					+ "issueSuccessRedirect() to work.");
//		}
//		StringBuilder targetUrl = new StringBuilder();
//		if (contextRelative && successUrl.startsWith("/")) {
//			targetUrl.append(request.getContextPath());
//		}
//		targetUrl.append(successUrl);
//		return targetUrl.toString();
//	}
//
//	public void setSaveRequestedUrlEnabled(boolean saveRequestedUrlEnabled) {
//		this.saveRequestedUrlEnabled = saveRequestedUrlEnabled;
//	}
//
//	public boolean isSaveRequestedUrlEnabled() {
//		return saveRequestedUrlEnabled;
//	}
//
//	public String getLoginFailureMessageAttribute() {
//		return loginFailureMessageAttribute;
//	}
//
//	public void setLoginFailureMessageAttribute(String loginFailureMessageAttribute) {
//		this.loginFailureMessageAttribute = loginFailureMessageAttribute;
//	}
	
	

}
