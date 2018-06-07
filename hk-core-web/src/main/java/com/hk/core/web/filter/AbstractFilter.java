package com.hk.core.web.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author: huangkai
 * @date 2018-06-07 17:17
 */
public abstract class AbstractFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    /**
     *
     */
    @Override
    public final void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        doInternal((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }

    protected abstract void doInternal(HttpServletRequest request, HttpServletResponse response,
                                       FilterChain chain) throws IOException, ServletException;

    @Override
    public void destroy() {

    }
}
