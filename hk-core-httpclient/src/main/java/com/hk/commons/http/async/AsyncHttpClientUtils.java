package com.hk.commons.http.async;

import com.hk.commons.http.ClientIdleConnectionReaper;
import com.hk.commons.http.client.CustomConnectionKeepAliveStrategy;
import com.hk.commons.http.utils.HttpUtils;
import com.hk.commons.util.ArrayUtils;
import com.hk.commons.util.CollectionUtils;
import com.hk.commons.util.JsonUtils;
import lombok.SneakyThrows;
import org.apache.http.*;
import org.apache.http.auth.AuthSchemeProvider;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.Lookup;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.impl.auth.*;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.message.BasicHeader;
import org.apache.http.nio.conn.NoopIOSessionStrategy;
import org.apache.http.nio.conn.SchemeIOSessionStrategy;
import org.apache.http.nio.conn.ssl.SSLIOSessionStrategy;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

import java.nio.charset.CodingErrorAction;
import java.util.List;
import java.util.Map;

/**
 * http client 异步 工具类
 *
 * @author kevin
 * @date 2019-8-9 15:00
 */
public abstract class AsyncHttpClientUtils {

    private final static Lookup<AuthSchemeProvider> authSchemeRegistry;

    private final static PoolingNHttpClientConnectionManager connectionManager;

    static {
        IOReactorConfig ioReactorConfig = IOReactorConfig.custom()
                .setSoKeepAlive(false)
                .setTcpNoDelay(true)
                .setIoThreadCount(Runtime.getRuntime().availableProcessors())
                .build();
        Registry<SchemeIOSessionStrategy> sessionStrategyRegistry = RegistryBuilder
                .<SchemeIOSessionStrategy>create()
                .register("http", NoopIOSessionStrategy.INSTANCE)
                .register("https", new SSLIOSessionStrategy(SSLContexts.createDefault()))
                .build();

        authSchemeRegistry = RegistryBuilder
                .<AuthSchemeProvider>create()
                .register(AuthSchemes.BASIC, new BasicSchemeFactory(Consts.UTF_8))
                .register(AuthSchemes.DIGEST, new DigestSchemeFactory(Consts.UTF_8))
                .register(AuthSchemes.NTLM, new NTLMSchemeFactory())
                .register(AuthSchemes.SPNEGO, new SPNegoSchemeFactory())
                .register(AuthSchemes.KERBEROS, new KerberosSchemeFactory())
                .build();
        try {
            ConnectingIOReactor ioReactor = new DefaultConnectingIOReactor(ioReactorConfig);
            connectionManager = new PoolingNHttpClientConnectionManager(ioReactor, sessionStrategyRegistry);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        connectionManager.setMaxTotal(500);
        connectionManager.setDefaultMaxPerRoute(50);
        connectionManager.setDefaultConnectionConfig(ConnectionConfig.custom()
                .setMalformedInputAction(CodingErrorAction.IGNORE)
                .setUnmappableInputAction(CodingErrorAction.IGNORE)
                .setCharset(Consts.UTF_8).build());
        ClientIdleConnectionReaper.registerNConnectionManager(connectionManager);

    }

    public static CloseableHttpAsyncClient createDefaultHttpAsyncClient() {
        return createAsyncHttpClient(HttpUtils.DEFAULT_REQUEST_CONFIG);
    }

    public static CloseableHttpAsyncClient createAsyncHttpClient(RequestConfig requestConfig) {
        return HttpAsyncClients.custom()
                .setConnectionManager(connectionManager)
                .setDefaultAuthSchemeRegistry(authSchemeRegistry)
                .setKeepAliveStrategy(CustomConnectionKeepAliveStrategy.getInstance())
                .setDefaultRequestConfig(requestConfig)
                .build();
    }

    /**
     * http Get 异步调用
     *
     * @param uri            请求地址
     * @param params         请求参数
     * @param futureCallback 异步回调，如果 为 null　时，直接返回结果
     * @param headers        　请求头
     * @return 结果，如果 futureCallback 不为 null，结果需要在此回调中获得
     */
    public static String get(String uri, Map<String, ?> params, FutureCallback<HttpResponse> futureCallback, Header... headers) {
        List<Header> headerList = ArrayUtils.asArrayList(HttpUtils.DEFAULT_HEADER.get(0));
        CollectionUtils.addAllNotNull(headerList, headers);
        return execute(createDefaultHttpAsyncClient(),
                HttpUtils.newHttpGet(uri, params, headerList.toArray(new Header[0])),
                futureCallback);
    }

    /**
     * http Delete 异步调用
     *
     * @param uri            请求地址
     * @param params         请求参数
     * @param futureCallback 异步回调，如果 为 null　时，直接返回结果
     * @param headers        　请求头
     * @return 结果，如果 futureCallback 不为 null，结果需要在此回调中获得
     */
    public static String delete(String uri, Map<String, ?> params, FutureCallback<HttpResponse> futureCallback, Header... headers) {
        List<Header> headerList = ArrayUtils.asArrayList(HttpUtils.DEFAULT_HEADER.get(0));
        CollectionUtils.addAllNotNull(headerList, headers);
        return execute(createDefaultHttpAsyncClient(),
                HttpUtils.newHttpDelete(uri, params, headerList.toArray(new Header[0])),
                futureCallback);
    }

    @SneakyThrows
    public static String execute(CloseableHttpAsyncClient asyncClient, HttpUriRequest request, FutureCallback<HttpResponse> futureCallback) {
        try (CloseableHttpAsyncClient asyncHttpClient = asyncClient) {
            asyncClient.start();
            HttpResponse httpResponse = asyncHttpClient.execute(request, futureCallback).get();
            StatusLine statusLine = httpResponse.getStatusLine();
            if (statusLine.getStatusCode() < 300) {
                if (null == futureCallback) {
                    return EntityUtils.toString(httpResponse.getEntity(), Consts.UTF_8);
                }
            }
            if (null == futureCallback) {
                throw new HttpResponseException(statusLine.getStatusCode(),
                        statusLine.getReasonPhrase());
            }
            return null;
        }
    }

    /**
     * 常规 http Post 异步调用
     *
     * @param uri            请求地址
     * @param params         请求参数
     * @param futureCallback 异步回调，如果 为 null　时，直接返回结果
     * @param headers        　请求头
     * @return 结果，如果 futureCallback 不为 null，结果需要在此回调中获得
     */
    public static String simplePost(String uri, Map<String, ?> params, FutureCallback<HttpResponse> futureCallback, Header... headers) {
        List<Header> headerList = ArrayUtils.asArrayList(HttpUtils.DEFAULT_HEADER.get(0));
        CollectionUtils.addAllNotNull(headerList, headers);
        return execute(createDefaultHttpAsyncClient(),
                HttpUtils.newHttpPost(uri, HttpUtils.toHttpEntity(params), headerList.toArray(new Header[0])),
                futureCallback);
    }

    /**
     * json http Post 异步调用，添加请求头： Content-Type:application/json;charset=utf-8
     *
     * @param uri            请求地址
     * @param params         请求参数
     * @param futureCallback 异步回调，如果 为 null　时，直接返回结果
     * @param headers        　请求头
     * @return 结果，如果 futureCallback 不为 null，结果需要在此回调中获得
     */
    public static String jsonPost(String uri, Object body, FutureCallback<HttpResponse> futureCallback, Header... headers) {
        List<Header> headerList = ArrayUtils.asArrayList(HttpUtils.DEFAULT_HEADER.get(0),
                new BasicHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString()));
        CollectionUtils.addAllNotNull(headerList, headers);
        return execute(createDefaultHttpAsyncClient(),
                HttpUtils.newHttpPost(uri, new NStringEntity(JsonUtils.serialize(body), Consts.UTF_8), headerList.toArray(new Header[0])),
                futureCallback);
    }

    /**
     * 附件 http Post 异步调用
     *
     * @param uri            请求地址
     * @param params         请求参数
     * @param futureCallback 异步回调，如果 为 null　时，直接返回结果
     * @param headers        　请求头
     * @return 结果，如果 futureCallback 不为 null，结果需要在此回调中获得
     */
    public static String mimePost(String uri, Map<String, ContentBody> contentBody, FutureCallback<HttpResponse> futureCallback, Header... headers) {
        List<Header> headerList = ArrayUtils.asArrayList(HttpUtils.DEFAULT_HEADER.get(0));
        CollectionUtils.addAllNotNull(headerList, headers);
        HttpPost httpPost = HttpUtils.newHttpPost(uri, HttpUtils.contentBodyToHttpEntity(contentBody), headerList.toArray(new Header[0]));
        return execute(createDefaultHttpAsyncClient(), httpPost, futureCallback);
    }


}
