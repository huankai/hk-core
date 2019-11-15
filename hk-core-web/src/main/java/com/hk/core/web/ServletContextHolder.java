/**
 *
 */
package com.hk.core.web;

import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;
import java.net.URL;

/**
 * @author kevin
 * @date 2017年12月22日上午9:02:01
 */
public class ServletContextHolder implements ServletContextAware {

    private static ServletContext servletContext;

    @Override
    public void setServletContext(ServletContext servletContext) {
        ServletContextHolder.servletContext = servletContext;
    }

    /**
     * @param path path
     * @return Resource
     */
    public static URL getResource(String path) {
        try {
            return servletContext.getResource(path);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * get ServletContext
     *
     * @return ServletContext
     */
    public static ServletContext getServletContext() {
        return servletContext;
    }

    /**
     * @return contextPath
     */
    public static String getContextPath() {
        return servletContext.getContextPath();
    }

}
