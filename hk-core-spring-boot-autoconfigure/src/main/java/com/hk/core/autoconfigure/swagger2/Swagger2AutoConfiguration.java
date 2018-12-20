package com.hk.core.autoconfigure.swagger2;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger 接口文档自动配置
 *
 * @author kevin
 * @date 2018-09-14 12:39
 */
@Configuration
@EnableSwagger2
@ConditionalOnClass(Docket.class)
@ConditionalOnProperty(prefix = "hk.swagger", value = "enable", havingValue = "true")
@EnableConfigurationProperties(SwaggerProperties.class)
public class Swagger2AutoConfiguration {

    private SwaggerProperties swaggerProperties;

    public Swagger2AutoConfiguration(SwaggerProperties properties) {
        this.swaggerProperties = properties;
    }

    @Bean
    public Docket controllerApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        .title(swaggerProperties.getTitle())
                        .description(swaggerProperties.getDescription())
                        .version(swaggerProperties.getVersion())
                        .build())
                .select()
                .apis(RequestHandlerSelectors.basePackage(swaggerProperties.getBasePackage()))
                .paths(PathSelectors.any())
                .build();
    }
}
