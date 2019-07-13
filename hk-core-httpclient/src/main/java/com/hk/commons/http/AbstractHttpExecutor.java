package com.hk.commons.http;

import com.hk.commons.util.ArrayUtils;
import com.hk.commons.util.CollectionUtils;
import com.hk.commons.util.ConverterUtils;
import com.hk.commons.util.StringUtils;
import lombok.Setter;
import org.apache.http.*;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.config.SocketConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

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
    public static final RequestConfig DEFAULT_REQUEST_CONFIG = RequestConfig.custom()
            .setSocketTimeout(10000)
            .setConnectTimeout(5000)
            .setConnectionRequestTimeout(5000)
            .build();


    /**
     * httpClient
     */
    @Setter
    private CloseableHttpClient httpClient;

    private CloseableHttpAsyncClient asyncClient;

    @Setter
    private FutureCallback<HttpResponse> futureCallback;

    protected CloseableHttpAsyncClient getAsyncClient() {
        if (null == this.asyncClient) {
            this.asyncClient = HttpAsyncClients.custom().useSystemProperties()
                    .setMaxConnTotal(50)
                    .disableCookieManagement()
                    .useSystemProperties()
                    .build();
        }
        return asyncClient;
    }

    protected CloseableHttpClient getHttpClient() {
        if (this.httpClient == null) {
            PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
            connectionManager.setMaxTotal(500);
            connectionManager.setDefaultMaxPerRoute(50);
            SocketConfig socketConfig = SocketConfig.custom().setSoTimeout(1000 * 5).build();
            connectionManager.setDefaultSocketConfig(socketConfig);
            this.httpClient = HttpClients.
                    custom()
//                    .setUserAgent("")//自定义 user-Agent
                    .setConnectionManager(connectionManager)
                    .setConnectionManagerShared(true)
                    .setDefaultRequestConfig(DEFAULT_REQUEST_CONFIG)
                    .build();
        }
        return httpClient;
    }

    @Setter
    private boolean async = false;

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

    protected T doExecute(HttpUriRequest request) throws IOException {
        if (async) {
            try (CloseableHttpAsyncClient client = getAsyncClient()) {
                client.start();
                Future<HttpResponse> future = client.execute(request, HttpClientContext.create(), futureCallback);
                future.get();
                return null;
            } catch (InterruptedException | ExecutionException e) {
                throw new IOException(e);
            }
        } else {
            try (CloseableHttpClient client = getHttpClient()) {
                return client.execute(request, responseHandler);
            }
        }
    }

    /**
     * 生成url
     *
     * @param uri    请求URI
     * @param params 请求参数
     * @return url?params1[key]=params1[value]&params2[key]=params2[value]
     */
    protected String generateUri(URI uri, Map<String, Object> params) {
        String urlString = uri.toString();
        StringBuilder s = new StringBuilder();
        s.append(urlString);
        if (CollectionUtils.isNotEmpty(params)) {
            List<NameValuePair> nvps = new ArrayList<>();
            params.forEach((key, value) -> {
                String convertValue = ConverterUtils.defaultConvert(value, String.class);
                if (StringUtils.isNotEmpty(convertValue)) {
                    nvps.add(new BasicNameValuePair(key, convertValue));
                }
            });
            if (!nvps.isEmpty()) {
                if (!StringUtils.endsWith(urlString, "?")) {
                    s.append("?");
                }
                s.append(URLEncodedUtils.format(nvps, Consts.UTF_8));
            }
        }
        return s.toString();
    }
}
