package com.hk.core.authentication.oauth2.configuration;

import com.hk.commons.util.StringUtils;
import lombok.NoArgsConstructor;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;

/**
 * 通过 web.xml 配置文件中获取属性值
 *
 * @author kevin
 * @date 2019-5-6 16:38
 */
@NoArgsConstructor
public class WebXmlConfigurationStrategyImpl extends BaseConfigurationStrategy {

    private FilterConfig filterConfig;

    @Override
    protected String get(ConfigurationKey<?> configurationKey) {
        final String value = this.filterConfig.getInitParameter(configurationKey.getName());
        if (StringUtils.isNotEmpty(value)) {
            logger.info("Property [{}] loaded from FilterConfig.getInitParameter with value [{}]", configurationKey, value);
            return value;
        }

        final String value2 = filterConfig.getServletContext().getInitParameter(configurationKey.getName());
        if (StringUtils.isNotEmpty(value2)) {
            logger.info("Property [{}] loaded from ServletContext.getInitParameter with value [{}]", configurationKey, value2);
            return value2;
        }
        return null;
    }

    @Override
    public void init(FilterConfig filterConfig, Class<? extends Filter> filterClazz) {
        this.filterConfig = filterConfig;
    }
}
