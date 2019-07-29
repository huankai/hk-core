package com.hk.core.web;

import com.hk.commons.util.Contants;
import com.hk.commons.util.FileUtils;
import com.hk.commons.util.JsonUtils;
import com.hk.commons.util.StringUtils;
import lombok.SneakyThrows;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * web相关的工具类
 *
 * @author kevin
 * @date 2017年9月22日下午2:51:02
 */
public abstract class Webs {

    private static final String MSIE_USER_AGENT_HEADER_VALUE = "MSIE";

    private static final String EDGE_USER_AGENT_HEADER_VALUE = "Edge";

    private static final String TRIDENT_USER_AGENT_HEADER_VALUE = "Trident";

    private static final String MOZILLA_USER_AGENT_HEADER_VALUE = "Mozilla";

    /**
     * 获取request对象
     *
     * @return HttpServletRequest
     */
    public static HttpServletRequest getHttpServletRequest() {
        return getRequestAttribute().getRequest();
    }

    /**
     * 获取response对象
     *
     * @return HttpServletResponse
     */
    public static HttpServletResponse getHttpServletResponse() {
        return getRequestAttribute().getResponse();
    }

    /**
     * 从request请求域中获取属性值
     *
     * @param name name
     * @return request attribute by give name.
     */
    public static <T> T getAttributeFromRequest(String name, Class<T> clazz) {
        return getAttribute(name, RequestAttributes.SCOPE_REQUEST, clazz);
    }

    /**
     * 从Session请求域中获取属性值
     *
     * @param name name
     * @return session value by give name
     */
    public static <T> T getAttributeFromSession(String name, Class<T> clazz) {
        return getAttribute(name, RequestAttributes.SCOPE_SESSION, clazz);
    }

    /**
     * 从Session请求域中设置属性值
     *
     * @param name  name
     * @param value value
     */
    public static void setAttributeFromSession(String name, Object value) {
        setAttributeFromSession(name, value, false);
    }

    /**
     * 从Session请求域中设置属性值
     *
     * @param name   name
     * @param value  value
     * @param create Whether to create session
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
     * @param name name
     */
    public static void removeAttributeFromSession(String name) {
        getRequestAttribute().removeAttribute(name, RequestAttributes.SCOPE_SESSION);
    }

    /**
     * 获取属性值
     *
     * @param name  name
     * @param scope scope
     * @return Value
     */
    public static <T> T getAttribute(String name, int scope, Class<T> clazz) throws ClassCastException {
        ServletRequestAttributes requestAttribute = getRequestAttribute();
        if (requestAttribute == null) {
            return null;
        }
        Object value = requestAttribute.getAttribute(name, scope);
        return value == null ? null : clazz.cast(value);
    }

    private static ServletRequestAttributes getRequestAttribute() {
        return (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    }

    /**
     * 是否为ajax请求
     *
     * @param request request
     * @return true if Request Header contains X-Requested-With
     */
    public static boolean isAjax(HttpServletRequest request) {
        return StringUtils.isNotEmpty(request.getHeader("X-Requested-With"));
    }

//    /**
//     * 是否为ajax请求
//     *
//     * @return true if Request Header contains X-Requested-With
//     */
//    public static boolean isAjax() {
//        return isAjax(getHttpServletRequest());
//    }

    /**
     * 是否是微信浏览器请求
     *
     * @param request request
     * @return true if Request Header contains MicroMessenger
     */
    public static boolean isWeiXin(HttpServletRequest request) {
        return StringUtils.contains(request.getHeader(HttpHeaders.USER_AGENT), "MicroMessenger");
    }

    /**
     * 判断是否是支付宝发出的请求
     *
     * @param request request
     * @return true or false
     */
    public static boolean isAliPay(HttpServletRequest request) {
        return StringUtils.contains(request.getHeader(HttpHeaders.USER_AGENT), "AlipayClient");
    }

    /**
     * 判断是否是 android 发出的请求
     *
     * @param request request
     * @return true or false
     */
    public static boolean isAndroid(HttpServletRequest request) {
        return StringUtils.contains(request.getHeader(HttpHeaders.USER_AGENT), "Android");
    }

    /**
     * 判断是否手机端请求
     *
     * @param request
     * @return
     */
    public static boolean isMobile(HttpServletRequest request) {
        return isAndroid(request) || isIPhone(request);
    }

    /**
     * 判断是否是 苹果手机应用发送的请求
     *
     * @param request request
     * @return true or false
     */
    public static boolean isIPhone(HttpServletRequest request) {
        return StringUtils.contains(request.getHeader(HttpHeaders.USER_AGENT), "iPhone");
    }

    /**
     * 文件下载或预览
     *
     * @param fileName fileName
     * @param body     body
     * @return ResponseEntity
     */
    public static ResponseEntity<InputStreamResource> toResponseEntity(String fileName, byte[] body) {
        return toResponseEntity(fileName, new ByteArrayResource(body));
    }

    /**
     * 文件下载或预览
     *
     * @param fileName fileName
     * @param resource resource
     * @return ResponseEntity
     */
    @SneakyThrows(value = {IOException.class})
    public static ResponseEntity<InputStreamResource> toResponseEntity(String fileName, Resource resource) {
        return toResponseEntity(fileName, resource.contentLength(), resource.getInputStream());
    }

    /**
     * 文件下载或预览
     *
     * @param fileName fileName
     * @param url      url
     * @return ResponseEntity
     */
    @SneakyThrows(value = {IOException.class})
    public static ResponseEntity<InputStreamResource> toResponseEntity(String fileName, URL url) {
        URLConnection connection = url.openConnection();
        return toResponseEntity(fileName, connection.getContentLength(), connection.getInputStream());
    }

    /**
     * 文件下载或预览
     *
     * @param resource resource
     * @return ResponseEntity
     */
    @SneakyThrows(value = {IOException.class})
    public static ResponseEntity<InputStreamResource> toResponseEntity(Resource resource) {
        return toResponseEntity(resource.getFilename(), MediaType.IMAGE_JPEG, resource.contentLength(),
                new InputStreamResource(resource.getInputStream()));
    }

    /**
     * 文件下载或预览
     *
     * @param fileName fileName
     * @param in       in
     * @return ResponseEntity
     */
    public static ResponseEntity<InputStreamResource> toResponseEntity(String fileName, long contextLength,
                                                                       InputStream in) {
        InputStreamResource streamResource = new InputStreamResource(in);
        MediaType mediaType = StringUtils.isEmpty(fileName)
                || (streamResource.isFile() && FileUtils.isImage(streamResource.getFilename()))
                ? MediaType.IMAGE_JPEG : MediaType.APPLICATION_OCTET_STREAM;
        return toResponseEntity(fileName, mediaType, contextLength, streamResource);
    }

    private static <T> ResponseEntity<T> toResponseEntity(String fileName, MediaType mediaType,
                                                          long contextLength, T body) {
        HttpHeaders httpHeaders = new HttpHeaders();
        if (StringUtils.isNotEmpty(fileName)) {
            httpHeaders.setContentDispositionFormData("attachment", obtainAttachFileName(fileName));
        }
        httpHeaders.setContentType(mediaType);
        httpHeaders.setContentLength(contextLength);
        return ResponseEntity.ok().headers(httpHeaders).body(body);
    }

    /**
     * 附件名
     *
     * @param fileName fileName
     * @return fileName
     */
    @SneakyThrows
    private static String obtainAttachFileName(String fileName) {
        String encodeFileName = fileName;
        HttpServletRequest request = getHttpServletRequest();
        String agent = request.getHeader(HttpHeaders.USER_AGENT);
        if (StringUtils.isNotEmpty(agent)) {
            if (agent.contains(EDGE_USER_AGENT_HEADER_VALUE) || agent.contains(MSIE_USER_AGENT_HEADER_VALUE)
                    || agent.contains(TRIDENT_USER_AGENT_HEADER_VALUE)) {// IE
                encodeFileName = URLEncoder.encode(fileName, Contants.UTF_8);
            } else if (agent.contains(MOZILLA_USER_AGENT_HEADER_VALUE)) {// 火狐,谷歌
                encodeFileName = StringUtils.newStringIso8859_1(StringUtils.getByteUtf8(fileName));
            }
        }
        return encodeFileName;
    }

    /**
     * 获取请求地址
     *
     * @param request request
     * @return ip address
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
            if (StringUtils.equalsIgnoreCase("unknown", ip) || StringUtils.isEmpty(ip)) {
                ip = request.getRemoteAddr();
            }
        }
        return ip;
    }

    /**
     * 获取 user-Agent 信息
     *
     * @param request request
     * @return User-Agent
     */
    public static String getUserAgent(HttpServletRequest request) {
        return request.getHeader(HttpHeaders.USER_AGENT);
    }

    /**
     * 写入Json数据
     *
     * @param response response
     * @param status   status
     * @param data     data
     */
    @SneakyThrows(value = {IOException.class})
    public static void writeJson(HttpServletResponse response, int status, Object data) {
        response.setCharacterEncoding(Contants.UTF_8);
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        try (PrintWriter writer = response.getWriter()) {
            response.setStatus(status);
            writer.write(JsonUtils.serialize(data));
        }
    }

    public static String getClearContextPathUri(HttpServletRequest request) {
        String contextPath = request.getContextPath();
        return StringUtils.isEmpty(contextPath) ? request.getRequestURI()
                : StringUtils.substring(request.getRequestURI(), contextPath.length());
    }
}
