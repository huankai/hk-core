package com.hk.oauth2.http;

/**
 * @author kevin
 * @date 2019-5-18 11:33
 */
public interface HttpClient {

    boolean sendMessageToEndPoint(HttpMessage message);
}
