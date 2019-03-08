package com.hk.core.web.filter;

import com.hk.core.web.XssHttpServletRequestWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * XssFilter
 *
 * @author kevin
 * @date 2018-06-07 17:18
 * *
 */
public class XssFilter extends AbstractFilter {

    @Override
    protected void doInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        chain.doFilter(new XssHttpServletRequestWrapper(request), response);
    }
}
