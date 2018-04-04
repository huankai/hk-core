package com.hk.core.web.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 转换为 HttpServletRequest and HttpServletResponse
 *
 * @author huangkai
 * @date 2017年10月25日上午8:51:12
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
