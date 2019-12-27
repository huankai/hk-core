package com.hk.commons.http;

import com.hk.commons.http.client.CustomConnectionKeepAliveStrategy;
import com.hk.commons.http.utils.HttpUtils;
import com.hk.commons.util.ArrayUtils;
import com.hk.commons.util.CollectionUtils;
import com.hk.commons.util.JsonUtils;
import com.hk.commons.util.StringUtils;
import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpHost;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.SocketConfig;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * @author kevin
 * @date 2019-8-9 15:58
 */
public abstract class HttpClientUtils {

    private final static PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();

    static final CloseableHttpClient DEFAULT_HTTP_CLIENT;

    static {
        connectionManager.setMaxTotal(500);
        connectionManager.setDefaultConnectionConfig(ConnectionConfig
                .custom()
                .setCharset(StandardCharsets.UTF_8)
                .build());
        SocketConfig socketConfig = SocketConfig
                .custom()
                .setSoTimeout(30000)
                .setSoReuseAddress(true)
                .build();
        connectionManager.setDefaultSocketConfig(socketConfig);
        connectionManager.setDefaultMaxPerRoute(50);
        ClientIdleConnectionReaper.registerConnectionManager(connectionManager);
        DEFAULT_HTTP_CLIENT = createHttpClient(HttpUtils.DEFAULT_REQUEST_CONFIG);
    }

    public static CloseableHttpClient createHttpClient(RequestConfig requestConfig) {
        return createHttpClient(requestConfig, null);
    }

    public static CloseableHttpClient createHttpClient(RequestConfig requestConfig, String proxyHost) {
        return HttpClients.custom()
                .setProxy(StringUtils.isEmpty(proxyHost) ? null : HttpHost.create(proxyHost))
                .setConnectionManager(connectionManager)
                // 使用共享 connectionManager 需要设置为 true ，如果设置为 false，则每个 CloseableHttpClient 对象关闭后不能再使用
                .setConnectionManagerShared(true)
                .useSystemProperties()
                .setKeepAliveStrategy(CustomConnectionKeepAliveStrategy.getInstance())
                .setDefaultRequestConfig(requestConfig).build();
    }

    /**
     * Http get 请求
     *
     * @param uri     请求地址
     * @param params  请求参数
     * @param headers 请求头
     * @return 字符串表现形式的响应内容
     * @see com.hk.commons.http.get.HttpGetHttpExecutor
     */
    public static String get(String uri, Map<String, ?> params, Header... headers) throws IOException {
        List<Header> headerList = ArrayUtils.asArrayList(HttpUtils.DEFAULT_HEADER.get(0));
        CollectionUtils.addAllNotNull(headerList, headers);
        return execute(DEFAULT_HTTP_CLIENT,
                HttpUtils.newHttpGet(uri, params, headerList.toArray(new Header[0])));
    }

    /**
     * Http delete 请求
     *
     * @param uri     请求地址
     * @param params  请求参数
     * @param headers 请求头
     * @return 字符串表现形式的响应内容
     * @see com.hk.commons.http.delete.HttpDeleteHttpExecutor
     */
    public static String delete(String uri, Map<String, ?> params, Header... headers) throws IOException {
        List<Header> headerList = ArrayUtils.asArrayList(HttpUtils.DEFAULT_HEADER.get(0));
        CollectionUtils.addAllNotNull(headerList, headers);
        return execute(DEFAULT_HTTP_CLIENT,
                HttpUtils.newHttpDelete(uri, params, headerList.toArray(new Header[0])));
    }

    /**
     * http 请求执行
     *
     * @param httpClient httpClient
     * @param request    request
     * @return 字符串表现形式的响应内容
     */
    public static String execute(CloseableHttpClient httpClient, HttpUriRequest request) throws IOException {
        return execute(httpClient, request, UTF8ResponseHandler.getInstance());
    }

    /**
     * http 请求执行
     *
     * @param request         request
     * @param responseHandler 响应数据处理器
     * @return 响应内容
     */
    public static <T> T execute(HttpUriRequest request, ResponseHandler<T> responseHandler) throws IOException {
        return execute(DEFAULT_HTTP_CLIENT, request, responseHandler);
    }

    /**
     * http 请求执行
     *
     * @param httpClient      httpClient
     * @param request         request 方式
     * @param responseHandler 响应数据处理器
     * @return 响应内容
     */
    public static <T> T execute(CloseableHttpClient httpClient, HttpUriRequest request, ResponseHandler<T> responseHandler) throws IOException {
        try (CloseableHttpClient closeableHttpClient = httpClient) {
            return closeableHttpClient.execute(request, responseHandler);
        }
    }

    /**
     * Http Post 请求
     *
     * @param uri     请求地址
     * @param params  请求参数
     * @param headers 请求头
     * @return
     * @see com.hk.commons.http.post.SimplePostHttpExecutor
     */
    public static String simplePost(String uri, Map<String, ?> params, Header... headers) throws IOException {
        List<Header> headerList = ArrayUtils.asArrayList(HttpUtils.DEFAULT_HEADER.get(0));
        CollectionUtils.addAllNotNull(headerList, headers);
        return execute(DEFAULT_HTTP_CLIENT, HttpUtils.newHttpPost(uri,
                HttpUtils.toHttpEntity(params), headerList.toArray(new Header[0])));
    }

    /**
     * Http Post 请求，请求头包含 Content-Type =  application/json;charset=utf-8
     *
     * @param uri     请求地址
     * @param body    请求体
     * @param headers 请求头
     * @return
     * @see com.hk.commons.http.post.JsonPostHttpExecutor
     */
    public static String jsonPost(String uri, Object body, Header... headers) throws IOException {
        List<Header> headerList = ArrayUtils.asArrayList(HttpUtils.DEFAULT_HEADER.get(0),
                new BasicHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString()));
        CollectionUtils.addAllNotNull(headerList, headers);
        return execute(DEFAULT_HTTP_CLIENT,
                HttpUtils.newHttpPost(uri, new StringEntity(JsonUtils.serialize(body), Consts.UTF_8), headerList.toArray(new Header[0])));
    }

    /**
     * Http Post 文件上传的请求
     *
     * @param uri         请求地址
     * @param contentBody 请求体
     * @param headers     请求头
     * @return
     * @see com.hk.commons.http.post.MimePostHttpExecutor
     */
    public static String mimePost(String uri, Map<String, ContentBody> contentBody, Header... headers) throws IOException {
        List<Header> headerList = ArrayUtils.asArrayList(HttpUtils.DEFAULT_HEADER.get(0));
        CollectionUtils.addAllNotNull(headerList, headers);
        return execute(DEFAULT_HTTP_CLIENT,
                HttpUtils.newHttpPost(uri, HttpUtils.contentBodyToHttpEntity(contentBody), headerList.toArray(new Header[0])));
    }

}
