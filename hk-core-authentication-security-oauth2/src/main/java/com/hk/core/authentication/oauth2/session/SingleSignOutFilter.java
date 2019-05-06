package com.hk.core.authentication.oauth2.session;

import com.hk.commons.util.BeanUtils;
import com.hk.commons.util.Contants;
import com.hk.core.authentication.oauth2.configuration.ConfigurationKeys;
import com.hk.core.authentication.oauth2.configuration.ConfigurationStrategy;
import com.hk.core.authentication.oauth2.configuration.ConfigurationStrategyName;
import com.hk.core.web.filter.AbstractFilter;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.zip.Inflater;

/**
 * @author kevin
 * @date 2019-4-27 11:50
 */
@Slf4j
public class SingleSignOutFilter extends AbstractFilter {

    private static final String CONFIGURATION_STRATEGY_KEY = "configurationStrategy";

    private static final SingleSignOutHandler HANDLER = new SingleSignOutHandler();

    private AtomicBoolean handlerInitialized = new AtomicBoolean(false);

    @Override
    public void init(FilterConfig filterConfig) {
        super.init(filterConfig);
        final String configurationStrategyName = filterConfig.getServletContext().getInitParameter(CONFIGURATION_STRATEGY_KEY);
        ConfigurationStrategy configurationStrategy = BeanUtils.instantiateClass(ConfigurationStrategyName.resolveToConfigurationStrategy(configurationStrategyName));
        configurationStrategy.init(filterConfig, getClass());
        HANDLER.setEagerlyCreateSessions(configurationStrategy.getBoolean(ConfigurationKeys.EAGERLY_CREATE_SESSIONS));
        HANDLER.setArtifactParameterName(configurationStrategy.getString(ConfigurationKeys.ARTIFACT_PARAMETER_NAME));
        HANDLER.setLogoutParameterName(configurationStrategy.getString(ConfigurationKeys.LOGOUT_PARAMETER_NAME));
        HANDLER.init();
        handlerInitialized.set(true);
    }

    @Override
    protected void doInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (!this.handlerInitialized.getAndSet(true)) {
            HANDLER.init();
        }
        if (HANDLER.process(request)) {
            chain.doFilter(request, response);
        }
    }


}
