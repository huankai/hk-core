package com.hk.core.web;

import com.hk.commons.JsonResult;
import com.hk.commons.util.Contants;
import com.hk.commons.util.FileUtils;
import com.hk.commons.util.JsonUtils;
import com.hk.commons.util.StringUtils;
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

    private static final String USER_AGENT_HEADER_NAME = "User-Agent";

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
    public static HttpServletResponse getHttpservletResponse() {
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
        return clazz.cast(getRequestAttribute().getAttribute(name, scope));
    }

    /**
     * session 失效
     */
    public static void invalidateSession() {
        getRequestAttribute().getRequest().getSession().invalidate();
    }

    /**
     * 获取SessionId
     *
     * @return Session Id
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
     * @param request request
     * @return true if Request Header contains X-Requested-With
     */
    public static boolean isAjax(HttpServletRequest request) {
        return null != request.getHeader("X-Requested-With");
    }

    /**
     * 是否为ajax请求
     *
     * @return true if Request Header contains X-Requested-With
     */
    public static boolean isAjax() {
        return isAjax(getHttpServletRequest());
    }

    /**
     * 是否是微信浏览器请求
     *
     * @param request request
     * @return true if Request Header contains MicroMessenger
     */
    public static boolean isWeiXin(HttpServletRequest request) {
        String userAgent = request.getHeader("user-agent");
        return StringUtils.contains(userAgent, "MicroMessenger");
    }

    /**
     * 下载文件
     *
     * @param fileName fileName
     * @param body     body
     * @return ResponseEntity
     */
    public static ResponseEntity<InputStreamResource> toDownloadResponseEntity(String fileName, byte[] body) {
        return toDownloadResponseEntity(fileName, new ByteArrayResource(body));
    }

    /**
     * 下载文件
     *
     * @param fileName fileName
     * @param url      url
     * @return ResponseEntity
     */
    public static ResponseEntity<InputStreamResource> toDownloadResponseEntity(String fileName, URL url) {
        try {
            URLConnection connection = url.openConnection();
            return toDownloadResponseEntity(fileName, connection.getContentLength(), connection.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 文件下载
     *
     * @param fileName fileName
     * @param resource resource
     * @return ResponseEntity
     */
    public static ResponseEntity<InputStreamResource> toDownloadResponseEntity(String fileName, Resource resource) {
        try {
            return toDownloadResponseEntity(fileName, resource.contentLength(), resource.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 图片预览
     *
     * @param resource resource
     * @return ResponseEntity
     */
    public static ResponseEntity<InputStreamResource> toImageViewResponseEntity(Resource resource) {
        try {
            return toDownloadResponseEntity(resource.getFilename(), MediaType.IMAGE_JPEG, resource.contentLength(),
                    new InputStreamResource(resource.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * @param fileName fileName
     * @param in       in
     * @return ResponseEntity
     */
    public static ResponseEntity<InputStreamResource> toDownloadResponseEntity(String fileName, long contextLength,
                                                                               InputStream in) {
        InputStreamResource streamResource = new InputStreamResource(in);
        MediaType mediaType = (streamResource.isFile() && FileUtils.isImage(streamResource.getFilename()))
                ? MediaType.IMAGE_JPEG : MediaType.APPLICATION_OCTET_STREAM;
        return toDownloadResponseEntity(fileName, mediaType, contextLength, streamResource);
    }

    private static <T> ResponseEntity<T> toDownloadResponseEntity(String fileName, MediaType mediaType,
                                                                  long contextLength, T body) {
        HttpHeaders httpHeaders = new HttpHeaders();
        if (StringUtils.isNotEmpty(fileName)) {
            httpHeaders.setContentDispositionFormData("attachment", getAttachFileName(fileName));
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
    private static String getAttachFileName(String fileName) {
        String encodeFileName = fileName;
        HttpServletRequest request = getHttpServletRequest();
        try {
            String agent = request.getHeader(USER_AGENT_HEADER_NAME);
            if (StringUtils.isNotEmpty(agent)) {
                if (agent.contains(EDGE_USER_AGENT_HEADER_VALUE) || agent.contains(MSIE_USER_AGENT_HEADER_VALUE)
                        || agent.contains(TRIDENT_USER_AGENT_HEADER_VALUE)) {// IE
                    encodeFileName = URLEncoder.encode(fileName, Contants.UTF_8);
                } else if (agent.contains(MOZILLA_USER_AGENT_HEADER_VALUE)) {// 火狐,谷歌
                    encodeFileName = StringUtils.newStringIso8859_1(StringUtils.getByteUtf8(fileName));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return encodeFileName;
    }

    /**
     * 获取请求地址
     *
     * @return ip address
     */
    public static String getRemoteAddr() {
        return getRemoteAddr(getHttpServletRequest());
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
     * 写入Json数据
     *
     * @param response response
     * @param status   status
     * @param result   result
     */
    public static <T> void writeJson(HttpServletResponse response, int status, JsonResult<T> result) {
        response.setCharacterEncoding(Contants.UTF_8);
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        try (PrintWriter writer = response.getWriter()) {
            response.setStatus(status);
            writer.write(JsonUtils.serialize(result));
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static String getClearContextPathUri(HttpServletRequest request) {
        String contextPath = request.getContextPath();
        return StringUtils.isEmpty(contextPath) ? request.getRequestURI()
                : StringUtils.substring(request.getRequestURI(), contextPath.length());
    }
}
