package com.hk.core.authentication.oauth2.configuration;

/**
 * @author kevin
 * @date 2019-5-6 14:59
 */
public interface ConfigurationKeys {

    /**
     * logoutParameterName
     */
    ConfigurationKey<String> LOGOUT_PARAMETER_NAME = new ConfigurationKey<>("logoutParameterName", "oauth2LogoutParameterName");

    /**
     * eagerlyCreateSessions
     */
    ConfigurationKey<Boolean> EAGERLY_CREATE_SESSIONS = new ConfigurationKey<>("eagerlyCreateSessions", Boolean.TRUE);

    /**
     * artifactParameterName
     */
    ConfigurationKey<String> ARTIFACT_PARAMETER_NAME = new ConfigurationKey<>("artifactParameterName", "ticket");
}
