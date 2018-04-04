package com.hk.core.web.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.annotation.Order;

import com.hk.core.web.XssHttpServletRequestWrapper;

/**
 * XssFilter
 *
 * @author huangkai
 * @date 2017年10月11日下午1:25:07
 */
@Order(1)
@WebFilter(filterName = "xssFilter", urlPatterns = "/*")
public class XssFilter extends AbstractFilter {

    @Override
    protected void doInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        chain.doFilter(new XssHttpServletRequestWrapper(request), response);
    }

    // private boolean isExcludeUrl(HttpServletRequest request) {
    // if (excludeUrls != null) {
    // for (String url : excludeUrls) {
    // if (request.getRequestURI().contains(url)) {
    // return true;
    // }
    // }
    // }
    // return false;
    // }

}
