package com.hk.core.web;

import com.hk.commons.util.ArrayUtils;
import com.hk.commons.util.StringUtils;
import org.apache.commons.text.StringEscapeUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * @author: kevin
 * @date 2018-06-07 17:19
 */
public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {

    /**
     * Constructs a request object wrapping the given request.
     *
     * @param request The request to wrap
     * @throws IllegalArgumentException if the request is null
     */
    public XssHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String getQueryString() {
        String queryString = super.getQueryString();
        if (StringUtils.isNotEmpty(queryString)) {
            queryString = StringEscapeUtils.escapeHtml4(queryString);
        }
        return queryString;
    }

    @Override
    public String getParameter(String name) {
        String value = super.getParameter(StringEscapeUtils.escapeHtml4(name));
        return StringUtils.isEmpty(value) ? value : StringEscapeUtils.escapeHtml4(value);
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] parameters = super.getParameterValues(name);
        if (ArrayUtils.isEmpty(parameters)) {
            return null;
        }
        for (int i = 0, length = parameters.length; i < length; i++) {
            parameters[i] = StringEscapeUtils.escapeHtml4(parameters[i]);
        }
        return parameters;
    }


    @Override
    public String getHeader(String name) {
        String value = super.getHeader(StringEscapeUtils.escapeHtml4(name));
        return StringUtils.isNotEmpty(value) ? StringEscapeUtils.escapeHtml4(value) : value;
    }

}
