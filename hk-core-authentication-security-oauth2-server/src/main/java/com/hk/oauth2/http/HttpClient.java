package com.hk.oauth2.http;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * http client
 *
 * @author kevin
 * @date 2019-5-18 11:33
 */
@Deprecated
public interface HttpClient {

    /**
     * 发送消息到单点服务器客户端，客户端执行退出操作
     *
     * @param message message
     * @return 发送是否成功
     */
    boolean sendMessageToEndPoint(HttpMessage message);

    /**
     * 是否为 有效的 URL
     *
     * @param url url
     * @return true or false
     */
    default boolean isValidEndPoint(String url) {
        try {
            return isValidEndPoint(new URL(url));
        } catch (MalformedURLException e) {
            return false;
        }
    }

    boolean isValidEndPoint(URL url);
}
