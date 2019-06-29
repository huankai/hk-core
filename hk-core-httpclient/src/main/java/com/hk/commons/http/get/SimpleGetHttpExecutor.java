package com.hk.commons.http.get;

import org.apache.http.client.ResponseHandler;

/**
 * Get请求
 *
 * @author kevin
 * @date 2017年9月28日上午9:31:04
 */
public class SimpleGetHttpExecutor extends AbstractGetHttpExecutor<String> {

    public SimpleGetHttpExecutor() {
        super(UTF8_HANDLER);
    }

    /**
     * 自定义一个 CloseableHttpClient 和  ResponseHandler
     *
     * @param handler    handler
     */
    public SimpleGetHttpExecutor(ResponseHandler<String> handler) {
        super(handler);
    }

}
