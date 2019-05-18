package com.hk.oauth2.logout;

/**
 * @author kevin
 * @date 2019-5-18 11:49
 */
public class SamlCompliantLogoutMessageCreator implements LogoutMessageCreator {

    private static final String LOGOUT_REQUEST_TEMPLATE = "<samlp:LogoutRequest xmlns:samlp=\"urn:oasis:names:tc:SAML:2.0:protocol\"><samlp:AccessToken>%s</samlp:AccessToken></samlp:LogoutRequest>";

    @Override
    public String create(String accessToken) {
        return String.format(LOGOUT_REQUEST_TEMPLATE, accessToken);
    }
}
