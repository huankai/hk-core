package com.hk.commons.http.post;

import com.hk.commons.http.AbstractHttpExecutor;
import org.apache.http.HttpEntity;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.IOException;
import java.net.URI;

/**
 * Post请求
 *
 * @param <T>
 * @param <P>
 * @author kevin
 */
public abstract class AbstractPostHttpExecutor<T, P> extends AbstractHttpExecutor<T, P> {

    public AbstractPostHttpExecutor(ResponseHandler<T> responseHandler) {
        super(responseHandler);
    }

    public AbstractPostHttpExecutor(CloseableHttpClient httpClient, ResponseHandler<T> responseHandler) {
        super(httpClient, responseHandler);
    }

    public T execute(URI uri, P params) throws IOException {
        HttpPost httpPost = new HttpPost();
        httpPost.setEntity(generateEntity(params));
        httpPost.setHeaders(getHeaders());
        httpPost.setURI(uri);
        return doExecute(httpPost);
    }

    public abstract HttpEntity generateEntity(P params);

}
