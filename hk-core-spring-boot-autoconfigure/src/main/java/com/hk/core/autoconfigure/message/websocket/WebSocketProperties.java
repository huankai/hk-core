package com.hk.core.autoconfigure.message.websocket;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * websocket 属性
 *
 * @author kevin
 * @date 2018-09-21 13:24
 */
@Data
@RefreshScope
@ConfigurationProperties(prefix = "hk.websocket")
public class WebSocketProperties {

    /**
     * endpoint
     */
    private String[] endpoint;

    /**
     * userDestinationPrefix
     */
    private String userDestinationPrefix;

    /**
     * allowedOrigins
     */
    private String[] allowedOrigins = new String[]{"*"};

    /**
     * simpleBrokers
     */
    private String[] simpleBrokers;

    /**
     * applicationDestinationPrefixes
     */
    private String[] applicationDestinationPrefixes;

    /**
     * originDisabled
     */
    private boolean originDisabled = true;

    /**
     * cacheLimit
     */
    private Integer cacheLimit;

}
