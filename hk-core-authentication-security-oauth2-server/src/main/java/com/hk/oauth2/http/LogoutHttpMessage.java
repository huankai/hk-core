package com.hk.oauth2.http;

import org.springframework.http.MediaType;

import java.net.URL;

/**
 * @author kevin
 * @date 2019-5-18 11:33
 */
public class LogoutHttpMessage extends HttpMessage {

    public LogoutHttpMessage(URL url, String message) {
        this(url, message, true);
    }

    public LogoutHttpMessage(URL url, String message, boolean async) {
        super(url, message, async);
        setContentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE);
    }

    @Override
    public String getMessage() {
        return "oauth2LogoutParameter=" + super.getMessage();
    }
}
