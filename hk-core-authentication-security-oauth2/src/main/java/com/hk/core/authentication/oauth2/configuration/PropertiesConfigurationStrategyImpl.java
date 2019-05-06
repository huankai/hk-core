package com.hk.core.authentication.oauth2.configuration;

import com.hk.commons.util.AssertUtils;
import com.hk.commons.util.StringUtils;
import lombok.NoArgsConstructor;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * <pre>
 *
 * 通过  properties 文件中获取属性值
 *  先从 自定义配置属性  configFileLocation 所配置的路径中获取属性值，如果文件不存在
 *  再从 /etc/java-oauth2-client.properties 目录获取属性值
 * </pre>
 *
 * @author kevin
 * @date 2019-5-6 16:42
 */
@NoArgsConstructor
public class PropertiesConfigurationStrategyImpl extends BaseConfigurationStrategy {

    /**
     * Property name we'll use in the {@link javax.servlet.FilterConfig} and {@link javax.servlet.ServletConfig} to try and find where
     * you stored the configuration file.
     */
    private static final String CONFIGURATION_FILE_LOCATION = "configFileLocation";

    /**
     * Default location of the configuration file.  Mostly for testing/demo.  You will most likely want to configure an alternative location.
     */
    private static final String DEFAULT_CONFIGURATION_FILE_LOCATION = "/etc/java-oauth2-client.properties";

    private String simpleFilterName;

    private Properties properties = new Properties();

    @Override
    protected String get(ConfigurationKey<?> configurationKey) {
        final String property = configurationKey.getName();
        final String filterSpecificProperty = this.simpleFilterName + "." + property;
        final String filterSpecificValue = this.properties.getProperty(filterSpecificProperty);
        if (StringUtils.isNotEmpty(filterSpecificValue)) {
            return filterSpecificValue;
        }
        return this.properties.getProperty(property);
    }

    @Override
    public void init(FilterConfig filterConfig, Class<? extends Filter> filterClazz) {
        this.simpleFilterName = filterClazz.getSimpleName();
        final String fileLocationFromFilterConfig = filterConfig.getInitParameter(CONFIGURATION_FILE_LOCATION);
        final boolean filterConfigFileLoad = loadPropertiesFromFile(fileLocationFromFilterConfig);
        if (!filterConfigFileLoad) {
            final String fileLocationFromServletConfig = filterConfig.getServletContext().getInitParameter(CONFIGURATION_FILE_LOCATION);
            final boolean servletContextFileLoad = loadPropertiesFromFile(fileLocationFromServletConfig);
            if (!servletContextFileLoad) {
                final boolean defaultConfigFileLoaded = loadPropertiesFromFile(DEFAULT_CONFIGURATION_FILE_LOCATION);
                AssertUtils.isTrue(defaultConfigFileLoaded, "unable to load properties to configure Oauth2 client");
            }
        }
    }

    private boolean loadPropertiesFromFile(final String file) {
        if (StringUtils.isEmpty(file)) {
            return false;
        }
        try (FileInputStream fis = new FileInputStream(file)) {
            this.properties.load(fis);
            return true;
        } catch (final IOException e) {
            logger.warn("Unable to load properties for file {}", file, e);
            return false;
        }
    }
}
