package com.hk.oauth2.http;

import lombok.Setter;
import lombok.SneakyThrows;
import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.client.*;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.client.*;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.FactoryBean;

import javax.net.ssl.HostnameVerifier;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 使用 apache http client 工具类
 *
 * @author kevin
 * @date 2019-5-18 14:29
 */
@Setter
public class SimpleHttpClientFactoryBean implements FactoryBean<SimpleHttpClient> {

    /**
     * 接受的默认状态代码：
     *
     * <pre>
     *     200
     *     202
     *     204
     *     304
     *     302
     *     301
     * </pre>
     */
    private static final int[] DEFAULT_ACCEPTABLE_CODES = new int[]{HttpURLConnection.HTTP_OK,
            HttpURLConnection.HTTP_NOT_MODIFIED, HttpURLConnection.HTTP_MOVED_TEMP,
            HttpURLConnection.HTTP_MOVED_PERM, HttpURLConnection.HTTP_ACCEPTED,
            HttpURLConnection.HTTP_NO_CONTENT};

    public static final int MAX_CONNECTIONS_PER_ROUTE = 50;

    private static final int MAX_POOLED_CONNECTIONS = 100;

    private static final int DEFAULT_THREADS_NUMBER = 200;

    /**
     * 默认连接超时时间：5 秒
     */
    private static final int DEFAULT_TIMEOUT = 5000;

    private static final int DEFAULT_QUEUE_SIZE = (int) (DEFAULT_THREADS_NUMBER * 0.2);

    /**
     * 线程数
     */
    private int threadsNumber = DEFAULT_THREADS_NUMBER;

    private int queueSize = DEFAULT_QUEUE_SIZE;

    private int maxPooledConnections = MAX_POOLED_CONNECTIONS;

    private int maxConnectionsPerRoute = MAX_CONNECTIONS_PER_ROUTE;

    /**
     * 连接超时
     */
    private int connectionTimeout = DEFAULT_TIMEOUT;

    private List<Integer> acceptableCodes = IntStream.of(DEFAULT_ACCEPTABLE_CODES).boxed().collect(Collectors.toList());

    private int readTimeout = DEFAULT_TIMEOUT;

    /**
     * 重定向
     */
    private RedirectStrategy redirectionStrategy = new DefaultRedirectStrategy();

    private SSLConnectionSocketFactory sslSocketFactory = SSLConnectionSocketFactory.getSocketFactory();

    private HostnameVerifier hostnameVerifier = new DefaultHostnameVerifier();

    private CredentialsProvider credentialsProvider;

    /**
     * cookie 认证
     */
    private CookieStore cookieStore;

    private ConnectionReuseStrategy connectionReuseStrategy = new DefaultConnectionReuseStrategy();

    private ConnectionBackoffStrategy connectionBackoffStrategy = new DefaultBackoffStrategy();

    private ServiceUnavailableRetryStrategy serviceUnavailableRetryStrategy = new DefaultServiceUnavailableRetryStrategy();

    /**
     * 请求头
     */
    private Collection<? extends Header> defaultHeaders = new ArrayList<>(0);

    private AuthenticationStrategy proxyAuthenticationStrategy = new ProxyAuthenticationStrategy();

    /**
     * 确定是否允许循环重定向
     */
    private boolean circularRedirectsAllowed = true;

    /**
     * 是否应给予认证
     */
    private boolean authenticationEnabled;

    /**
     * 是否应自动处理重定向
     */
    private boolean redirectsEnabled = true;

    private ExecutorService executorService;

    @Override
    public SimpleHttpClient getObject() {
        final var httpClient = buildHttpClient();
        final var requestExecutorService = buildRequestExecutorService(httpClient);
        final var codes = acceptableCodes.stream().sorted().collect(Collectors.toList());
        return new SimpleHttpClient(codes, httpClient, requestExecutorService);
    }

    private FutureRequestExecutionService buildRequestExecutorService(final CloseableHttpClient httpClient) {
        if (this.executorService == null) {
            this.executorService = new ThreadPoolExecutor(this.threadsNumber, this.threadsNumber, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(this.queueSize));
        }
        return new FutureRequestExecutionService(httpClient, this.executorService);
    }

    @SneakyThrows
    private CloseableHttpClient buildHttpClient() {
        final var registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", sslSocketFactory)
                .build();
        final var connectionManager = new PoolingHttpClientConnectionManager(registry);
        connectionManager.setMaxTotal(maxPooledConnections);
        connectionManager.setDefaultMaxPerRoute(maxConnectionsPerRoute);
        connectionManager.setValidateAfterInactivity(DEFAULT_TIMEOUT);
        final var httpHost = new HttpHost(InetAddress.getLocalHost());
        final var httpRoute = new HttpRoute(httpHost);
        connectionManager.setMaxPerRoute(httpRoute, MAX_CONNECTIONS_PER_ROUTE);
        final var requestConfig = RequestConfig
                .custom()
                .setSocketTimeout(readTimeout)
                .setConnectTimeout(connectionTimeout)
                .setConnectionRequestTimeout(connectionTimeout)
                .setCircularRedirectsAllowed(circularRedirectsAllowed)
                .setRedirectsEnabled(redirectsEnabled)
                .setAuthenticationEnabled(authenticationEnabled)
                .build();
        return HttpClients.custom()
                .setConnectionManager(connectionManager)
                .setDefaultRequestConfig(requestConfig)
                .setSSLSocketFactory(sslSocketFactory)
                .setSSLHostnameVerifier(hostnameVerifier)
                .setRedirectStrategy(redirectionStrategy)
                .setDefaultCredentialsProvider(credentialsProvider)
                .setDefaultCookieStore(cookieStore)
                .setConnectionReuseStrategy(connectionReuseStrategy)
                .setConnectionBackoffStrategy(connectionBackoffStrategy)
                .setServiceUnavailableRetryStrategy(serviceUnavailableRetryStrategy)
                .setProxyAuthenticationStrategy(proxyAuthenticationStrategy)
                .setDefaultHeaders(defaultHeaders)
                .useSystemProperties()
                .build();
    }

    @Override
    public Class<?> getObjectType() {
        return SimpleHttpClient.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
}
