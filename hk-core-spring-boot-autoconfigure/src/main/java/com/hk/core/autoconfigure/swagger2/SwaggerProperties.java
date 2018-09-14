package com.hk.core.autoconfigure.swagger2;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author: kevin
 * @date: 2018-09-14 12:42
 */
@Data
@ConfigurationProperties("hk.swagger")
public class SwaggerProperties {

    private String title;

    private String version;

    private String description;

    private String basePackage;
}
