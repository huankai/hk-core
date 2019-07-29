package com.hk.oauth2.http;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.RejectedExecutionException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.FutureRequestExecutionService;
import org.apache.http.impl.client.HttpRequestFutureTask;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.DisposableBean;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;

/**
 * @author kevin
 * @date 2019-5-18 11:34
 */
@Slf4j
@RequiredArgsConstructor
@SuppressWarnings("serial")
public class SimpleHttpClient implements HttpClient, Serializable, DisposableBean {

    /**
     *
     */
    private final List<Integer> acceptableCodes;

    /**
     *
     */
    private final transient CloseableHttpClient wrappedHttpClient;

    /**
     *
     */
    private final FutureRequestExecutionService requestExecutorService;

    @Override
    public boolean sendMessageToEndPoint(HttpMessage message) {
        try {
            final HttpPost request = new HttpPost(message.getUrl().toURI());
            request.addHeader(HttpHeaders.CONTENT_TYPE, message.getContentType());
            final StringEntity entity = new StringEntity(message.getMessage(), ContentType.create(message.getContentType()));
            request.setEntity(entity);
            final ResponseHandler<Boolean> handler = response -> response.getStatusLine().getStatusCode() == HttpStatus.SC_OK;
            log.debug("Created HTTP post message payload [{}]", request);
            final HttpRequestFutureTask<Boolean> task = requestExecutorService.execute(request, HttpClientContext.create(), handler);
            if (message.isAsynchronous()) {
                return true;
            }
            return task.get();
        } catch (final RejectedExecutionException e) {
            log.warn("Execution rejected", e);
            return false;
        } catch (final Exception e) {
            log.debug("Unable to send message", e);
            return false;
        }
    }

    @Override
    public boolean isValidEndPoint(URL url) {
        HttpEntity entity = null;
        try (CloseableHttpResponse response = wrappedHttpClient.execute(new HttpGet(url.toURI()))) {
            final int responseCode = response.getStatusLine().getStatusCode();

            final int idx = Collections.binarySearch(acceptableCodes, responseCode);
            if (idx >= 0) {
                log.debug("Response code from server matched [{}].", responseCode);
                return true;
            }
            log.debug("Response code did not match any of the acceptable response codes. Code returned was [{}]", responseCode);
            if (responseCode == HttpStatus.SC_INTERNAL_SERVER_ERROR) {
                final String value = response.getStatusLine().getReasonPhrase();
                log.error("There was an error contacting the endpoint: [{}]; The error was:\n[{}]", url.toExternalForm(), value);
            }
            entity = response.getEntity();
        } catch (final Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            EntityUtils.consumeQuietly(entity);
        }
        return false;
    }

    @Override
    public void destroy() {
        try {
            if (requestExecutorService != null) {
                requestExecutorService.close();
            }
        } catch (final IOException e) {
            // ignore
        }
    }

}
