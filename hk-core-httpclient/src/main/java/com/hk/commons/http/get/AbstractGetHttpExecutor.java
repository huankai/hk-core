package com.hk.commons.http.get;

import com.hk.commons.http.AbstractHttpExecutor;
import com.hk.commons.http.HttpExecutor;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;

import java.io.IOException;
import java.net.URI;
import java.util.Map;


/**
 * Get请求
 *
 * @param <T>
 * @author kevin
 */
public abstract class AbstractGetHttpExecutor<T> extends AbstractHttpExecutor<T, Map<String, Object>> {

    protected AbstractGetHttpExecutor(ResponseHandler<T> responseHandler) {
        super(responseHandler);
    }

    @Override
    public T execute(URI uri, Map<String, Object> params) throws IOException {
        return doExecute(buildHttpGet(uri, params));
    }

    /**
     * 创建一个HttpGet
     *
     * @param uri    uri
     * @param params params
     * @return {@link HttpGet}
     */
    protected final HttpGet buildHttpGet(URI uri, Map<String, Object> params) {
        HttpGet get = new HttpGet();
        String uri_ = HttpExecutor.generateUri(uri, params);
        get.setHeaders(getHeaders());
        get.setURI(URI.create(uri_));
        return get;
    }

}
