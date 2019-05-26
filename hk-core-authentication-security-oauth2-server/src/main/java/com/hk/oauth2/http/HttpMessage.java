package com.hk.oauth2.http;

import com.hk.oauth2.utils.EncodingUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;

import java.io.Serializable;
import java.net.URL;

/**
 * Http 消息
 *
 * @author kevin
 * @date 2019-5-18 11:33
 */
@Slf4j
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
        this.message = formatOutputMessageInternal(message);
        this.asynchronous = async;
    }

    protected String formatOutputMessageInternal(final String message) {
        try {
            return EncodingUtils.urlEncode(message);
        } catch (final Exception e) {
            log.warn("Unable to encode URL " + message, e);
        }
        return message;
    }


}
