package com.hk.commons.http.delete;

import com.hk.commons.http.AbstractHttpExecutor;
import com.hk.commons.http.HttpExecutor;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpDelete;

import java.io.IOException;
import java.net.URI;
import java.util.Map;


/**
 * Delete请求
 *
 * @param <T>
 * @author kevin
 */
public abstract class AbstractDeleteHttpExecutor<T> extends AbstractHttpExecutor<T, Map<String, Object>> {

    protected AbstractDeleteHttpExecutor(ResponseHandler<T> responseHandler) {
        super(responseHandler);
    }

    @Override
    public T execute(URI uri, Map<String, Object> params) throws IOException {
        return doExecute(buildHttpDelete(uri, params));
    }

    protected HttpDelete buildHttpDelete(URI uri, Map<String, Object> params) {
        HttpDelete httpDelete = new HttpDelete();
        String uri_ = HttpExecutor.generateUri(uri, params);
        httpDelete.setURI(URI.create(uri_));
        return httpDelete;
    }

}
