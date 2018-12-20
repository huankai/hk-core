package com.hk.core.autoconfigure.swagger2;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author kevin
 * @date 2018-09-14 12:42
 */
@Data
@ConfigurationProperties("hk.swagger")
public class SwaggerProperties {

    /**
     * 是否启用 swagger文档
     */
    private boolean enable;

    /**
     * 标题
     */
    private String title;

    /**
     * 版本号
     */
    private String version;

    /**
     * 描述
     */
    private String description;

    /**
     * 指定包名
     */
    private String basePackage;
}
