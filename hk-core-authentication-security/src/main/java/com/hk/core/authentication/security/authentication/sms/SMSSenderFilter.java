package com.hk.core.authentication.security.authentication.sms;

import com.hk.commons.JsonResult;
import com.hk.commons.Status;
import com.hk.core.authentication.api.validatecode.ValidateCodeProcessor;
import com.hk.core.web.Webs;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 短信发送
 *
 * @author kevin
 * @date 2019-7-17 16:01
 */
public class SMSSenderFilter extends HttpFilter {

    private ValidateCodeProcessor validateCodeProcessor;

    private RequestMatcher requestMatcher;

    public SMSSenderFilter(String processesUrl, ValidateCodeProcessor validateCodeProcessor) {
        this.requestMatcher = new AntPathRequestMatcher(processesUrl);
        this.validateCodeProcessor = validateCodeProcessor;
    }

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (requestMatcher.matches(request)) {
            try {
                String result = validateCodeProcessor.create(new ServletWebRequest(request, response));
                Webs.writeJson(response, 200, new JsonResult<>(Status.SUCCESS, null, result));
                return;
            } catch (Exception e) {
                // ignore
            }
        }
        chain.doFilter(request, response);
    }
}
