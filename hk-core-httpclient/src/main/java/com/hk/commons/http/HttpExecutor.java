package com.hk.commons.http;

import org.apache.http.Header;
import org.apache.http.client.ResponseHandler;

import java.io.IOException;
import java.net.URI;

/**
 * Http 请求
 *
 * @author kevin
 */
public interface HttpExecutor<T, P> {

    /**
     * @param uri    请求的RUI
     * @param params 请求的参数
     * @return 返回Http请求的结果
     * @throws IOException 抛出IO异常
     */
    default T execute(String uri, P params) throws IOException {
        return execute(URI.create(uri), params);
    }

    T execute(URI uri, P params) throws IOException;

    /**
     * 设置返回 处理器
     *
     * @param responseHandler ResponseHandler
     * @return {@link HttpExecutor}
     */
    HttpExecutor<T, P> setResponseHandler(ResponseHandler<T> responseHandler);

    /**
     * 添加请求头
     *
     * @param headers 请求头信息
     * @return {@link HttpExecutor}
     */
    HttpExecutor<T, P> addHeaders(Header... headers);

}
