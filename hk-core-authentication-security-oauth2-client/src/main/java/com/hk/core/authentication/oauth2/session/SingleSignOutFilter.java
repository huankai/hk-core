package com.hk.core.authentication.oauth2.session;

import com.hk.core.web.filter.AbstractFilter;
import lombok.RequiredArgsConstructor;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author kevin
 * @date 2019-5-18 10:47
 */
@RequiredArgsConstructor 
public class SingleSignOutFilter extends AbstractFilter {

    private final SingleSignOutHandler singleSignOutHandler;

    @Override
    protected void doInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (singleSignOutHandler.process(request)) {
            chain.doFilter(request, response);
        }
    }
}
