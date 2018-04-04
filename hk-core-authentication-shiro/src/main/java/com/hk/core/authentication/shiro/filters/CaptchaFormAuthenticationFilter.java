package com.hk.core.authentication.shiro.filters;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;

/**
 * 验证码登陆
 * 
 * @author huangkai
 * @date 2017年10月25日上午10:03:07
 */
public class CaptchaFormAuthenticationFilter extends AjaxFormAuthenticationFilter {

	private static final String DEFAULT_CAPTCHA_FORM_NAME = "captcha";

	private String captchaFormName = DEFAULT_CAPTCHA_FORM_NAME;

	@Override
	protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
		AuthenticationToken token = createToken(request, response);
        if (token == null) {
            String msg = "createToken method implementation returned null. A valid non-null AuthenticationToken " +
                    "must be created in order to execute a login attempt.";
            throw new IllegalStateException(msg);
        }
//        String captcha = request.getParameter(captchaFormName);
//        if (!captchaCookieGenerator.validate((HttpServletRequest) request, (HttpServletResponse) response, captcha)) {
//            return onLoginFailure(token, new CaptchaErrorException(), request, response);
//        }
        try {
            Subject subject = getSubject(request, response);
            subject.login(token);
            return onLoginSuccess(token, subject, request, response);
        } catch (AuthenticationException e) {
            return onLoginFailure(token, e, request, response);
        }
	}

	public String getCaptchaFormName() {
		return captchaFormName;
	}

	public void setCaptchaFormName(String captchaFormName) {
		this.captchaFormName = captchaFormName;
	}

}
