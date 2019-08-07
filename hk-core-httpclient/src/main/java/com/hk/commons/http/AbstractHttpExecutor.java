package com.hk.commons.http;

import com.hk.commons.http.client.CustomConnectionKeepAliveStrategy;
import com.hk.commons.util.ArrayUtils;
import com.hk.commons.util.CollectionUtils;
import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.SocketConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 请求接口抽象实现
 *
 * @author kevin
 */
public abstract class AbstractHttpExecutor<T, P> implements HttpExecutor<T, P> {

    /**
     * 默认响应处理器
     */
    public static final ResponseHandler<String> UTF8_HANDLER = new UTF8ResponseHandler();

    /**
     * 请求时间配置
     * Socket超时 :10 秒
     * 连接超时:    5秒
     * 连接请求超时: 5秒
     */
    private static final RequestConfig DEFAULT_REQUEST_CONFIG = RequestConfig.custom()
            .setSocketTimeout(10000)
            .setConnectTimeout(5000)
            .setConnectionRequestTimeout(5000)
            .build();

    private final static PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();

    /**
     * http Client 是一个线程安全的类，不需要重复创建该对象
     */
    private static final CloseableHttpClient httpClient;

    static {
        connectionManager.setMaxTotal(500);
        connectionManager.closeExpiredConnections();
        connectionManager.setDefaultConnectionConfig(ConnectionConfig.custom().setCharset(StandardCharsets.UTF_8).build());
        SocketConfig socketConfig = SocketConfig.custom().setSoTimeout(30000)
                .setSoReuseAddress(true).build();
        connectionManager.setDefaultSocketConfig(socketConfig);
        connectionManager.setDefaultMaxPerRoute(50);
        ClientIdleConnectionReaper.registerConnectionManager(connectionManager);
        httpClient = getHttpClient(DEFAULT_REQUEST_CONFIG);
    }

    public static CloseableHttpClient getHttpClient(RequestConfig requestConfig) {
        return HttpClients.custom()
                .setConnectionManager(connectionManager)
                .setConnectionManagerShared(true)
                .setKeepAliveStrategy(CustomConnectionKeepAliveStrategy.getInstance())
                .setDefaultRequestConfig(requestConfig).build();
    }

    /**
     * 响应处理器
     */
    private ResponseHandler<T> responseHandler;

    /**
     * Http 请求头信息
     */
    private List<Header> headers = ArrayUtils.asArrayList(
            new BasicHeader(HttpHeaders.CONTENT_ENCODING, Consts.UTF_8.name()));

    public AbstractHttpExecutor(ResponseHandler<T> responseHandler) {
        this.responseHandler = responseHandler;
    }

    @Override
    public final HttpExecutor<T, P> addHeaders(Header... headers) {
        CollectionUtils.addAllNotNull(this.headers, headers);
        return this;
    }

    @Override
    public HttpExecutor<T, P> setResponseHandler(ResponseHandler<T> responseHandler) {
        this.responseHandler = responseHandler;
        return this;
    }

    protected final Header[] getHeaders() {
        return this.headers.toArray(new Header[0]);
    }

    protected final T doExecute(HttpUriRequest request) throws IOException {
        try {
            return httpClient.execute(request, responseHandler);
        } finally {
            httpClient.close();
        }
    }
}
