package com.hk.core.autoconfigure.message.websocket;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * @author kevin
 * @date 2018-09-21 13:24
 */
@Data
@ConfigurationProperties(prefix = "hk.websocket")
public class WebSocketProperties {

    private String[] endpoint;

    private String userDestinationPrefix;

    private String[] allowedOrigins = new String[]{"*"};

    private String[] simpleBrokers;

    private String[] applicationDestinationPrefixes;

    private boolean originDisabled = true;
    
    private Integer cacheLimit;

}
