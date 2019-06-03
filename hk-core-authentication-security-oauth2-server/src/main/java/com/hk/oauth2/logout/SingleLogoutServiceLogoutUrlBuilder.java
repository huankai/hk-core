package com.hk.oauth2.logout;

import java.net.URL;
import java.util.Collection;

/**
 * @author kevin
 * @date 2019-5-23 14:19
 */
public interface SingleLogoutServiceLogoutUrlBuilder {

    Collection<URL> determineLogoutUrl(String accessToken);
}
