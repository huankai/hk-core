package com.hk.oauth2.http;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.MediaType;

import java.io.Serializable;
import java.net.URL;

/**
 * @author huangkai
 * @date 2019-05-06 21:24
 */
@Getter
@Setter
public class HttpMessage implements Serializable {

    private static final boolean DEFAULT_ASYNCHRONOUS_CALLBACKS_ENABLED = true;

    /**
     * logout URL
     */
    private final URL url;

    /**
     * logout 参数
     */
    private final String message;

    /**
     * 是否异常发送请求到 client
     */
    private final boolean asynchronous;

    /**
     * 发送的消息请求头类型
     */
    private String contentType = MediaType.APPLICATION_FORM_URLENCODED_VALUE;

    public HttpMessage(final URL url, final String message) {
        this(url, message, DEFAULT_ASYNCHRONOUS_CALLBACKS_ENABLED);
    }

    public HttpMessage(final URL url, final String message, final boolean async) {
        this.url = url;
        this.message = message;
        this.asynchronous = async;
    }
}
