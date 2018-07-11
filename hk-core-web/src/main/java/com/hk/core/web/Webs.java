package com.hk.core.web;

import com.hk.commons.util.Contants;
import com.hk.commons.util.JsonUtils;
import com.hk.commons.util.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * web相关的工具类
 *
 * @author: kevin
 * @date 2017年9月22日下午2:51:02
 */
public abstract class Webs {

    /**
     * 获取request对象
     *
     * @return
     */
    public static HttpServletRequest getHttpServletRequest() {
        return getRequestAttribute().getRequest();
    }

    /**
     * 获取response对象
     *
     * @return
     */
    public static HttpServletResponse getHttpservletResponse() {
        return getRequestAttribute().getResponse();
    }

    /**
     * 从request请求域中获取属性值
     *
     * @param name
     * @return
     */
    public static <T> T getAttributeFromRequest(String name) {
        return getAttribute(name, RequestAttributes.SCOPE_REQUEST);
    }

    /**
     * 从Session请求域中获取属性值
     *
     * @param name
     * @return
     */
    public static <T> T getAttributeFromSession(String name) {
        return getAttribute(name, RequestAttributes.SCOPE_SESSION);
    }

    /**
     * 从Session请求域中设置属性值
     *
     * @param name
     */
    public static void setAttributeFromSession(String name, Object value) {
        setAttributeFromSession(name, value, false);

    }

    /**
     * 从Session请求域中设置属性值
     *
     * @param name
     */
    public static void setAttributeFromSession(String name, Object value, boolean create) {
        HttpSession session = getRequestAttribute().getRequest().getSession(create);
        if (null != session) {
            session.setAttribute(name, value);
        }
    }

    /**
     * 从Session请求域中设置属性值
     *
     * @param name
     */
    public static void removeAttributeFromSession(String name) {
        getRequestAttribute().removeAttribute(name, RequestAttributes.SCOPE_SESSION);
    }

    /**
     * 获取属性值
     *
     * @param name
     * @param scope
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T getAttribute(String name, int scope) {
        return (T) getRequestAttribute().getAttribute(name, scope);
    }

    /**
     * 获取SessionId
     *
     * @return
     */
    public static String getSessionId() {
        return getRequestAttribute().getSessionId();
    }

    private static ServletRequestAttributes getRequestAttribute() {
        return (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    }

    /**
     * 是否为ajax请求
     *
     * @param request
     * @return
     */
    public static boolean isAjax(HttpServletRequest request) {
        return null != request.getHeader("X-Requested-With");
    }

    /**
     * 是否为ajax请求
     *
     * @return
     */
    public static boolean isAjax() {
        return isAjax(getHttpServletRequest());
    }

    /**
     * 是否是微信浏览器请求
     *
     * @param request
     * @return
     */
    public static boolean isWeiXin(HttpServletRequest request) {
        String userAgent = request.getHeader("user-agent");
        return StringUtils.contains(userAgent, "MicroMessenger");
    }

    /**
     * @param fileName
     * @param body
     * @return
     */
    public static ResponseEntity<byte[]> toDownResponseEntity(String fileName, byte[] body) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentDispositionFormData("attachment", fileName);
        httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return new ResponseEntity<>(body, httpHeaders, HttpStatus.OK);
    }

    /**
     * 获取请求地址
     *
     * @return
     */
    public static String getRemoteAddr() {
        return getRemoteAddr(getHttpServletRequest());
    }

    /**
     * 获取请求地址
     *
     * @param request
     * @return
     */
    public static String getRemoteAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (StringUtils.isEmpty(ip)) {
            if (StringUtils.equalsIgnoreCase("unknown", ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (StringUtils.equalsIgnoreCase("unknown", ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (StringUtils.equalsIgnoreCase("unknown", ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (StringUtils.equalsIgnoreCase("unknown", ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (StringUtils.equalsIgnoreCase("unknown", ip)) {
                ip = request.getRemoteAddr();
            }
        }
        return ip;
    }

    /**
     * 写入Json数据
     *
     * @param response
     * @param status
     * @param result
     */
    public static void writeJson(HttpServletResponse response, int status, JsonResult result) {
        response.setCharacterEncoding(Contants.UTF_8);
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        try (PrintWriter writer = response.getWriter()) {
            response.setStatus(status);
            writer.write(JsonUtils.toJSONString(result));
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
