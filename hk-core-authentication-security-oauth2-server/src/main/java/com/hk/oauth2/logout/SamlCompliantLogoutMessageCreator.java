package com.hk.oauth2.logout;

/**
 * @author kevin
 * @date 2019-5-18 11:49
 */
@Deprecated
public class SamlCompliantLogoutMessageCreator implements LogoutMessageCreator {

    private static final String LOGOUT_REQUEST_TEMPLATE = "<samlp:LogoutRequest xmlns:samlp=\"urn:oasis:names:tc:SAML:2.0:protocol\" ID=\"%s\"><samlp:AccessToken>%s</samlp:AccessToken></samlp:LogoutRequest>";

    @Override
    public String create(LogoutRequest logoutRequest) {
        return String.format(LOGOUT_REQUEST_TEMPLATE, logoutRequest.getClientId(), logoutRequest.getAccessToken());
    }
}
